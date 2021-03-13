package newhorizon.block.special;

import arc.Core;
import arc.Events;
import arc.audio.Sound;
import arc.func.Cons2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.audio.SoundLoop;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Tex;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Stat;
import newhorizon.content.NHFx;
import newhorizon.feature.Cube;
import newhorizon.feature.UpgradeData;
import newhorizon.feature.UpgradeData.DataEntity;
import newhorizon.func.NHSetting;
import newhorizon.func.TableFuncs;
import newhorizon.func.TextureFilterValue;
import newhorizon.interfaces.ScalableBlockc;
import newhorizon.interfaces.Scalablec;
import newhorizon.interfaces.Upgraderc;

import static mindustry.Vars.*;

public class UpgradeBlock extends Block {
	public static final int defaultID = -1;
	public static final Seq<UpgradeBlockBuild> upgradecGroup = new Seq<>(UpgradeBlockBuild.class);
	public static final int buttonPerLine = 8;
	
	protected Sound upgradeSound = Sounds.build;
	public int   maxLevel = 9;
	public float upgradeEffectChance = 0.04f;

	public Block linkTarget;
	public Color baseColor = Pal.accent;
	public Effect upgradeEffect = NHFx.upgrading;
	public float range = 400f;
	
	public final Seq<UpgradeData> upgradeDatas = new Seq<>();
	protected void addUpgrades(UpgradeData... inputs) {
		for(UpgradeData data : inputs){
			upgradeDatas.add(data);
			Log.info(data.toString());
		}
		Log.info("All added");
	}

	public UpgradeBlock(String name) {
		super(name);
		update = true;
		buildCostMultiplier = 2;
		configurable = true;
		solid = true;
		
		config(Point2.class, (Cons2<UpgradeBlockBuild, Point2>)UpgradeBlockBuild::linkPos);
		config(Integer.class, (Cons2<UpgradeBlockBuild, Integer>)UpgradeBlockBuild::upgradeData);
	}
	
	@Override
	public void init(){
		super.init();
		if(linkTarget == null || !(linkTarget instanceof ScalableBlockc) || !(linkTarget.buildType.get() instanceof Scalablec))throw new IllegalArgumentException("null @linkTarget :[red]'" + name + "'[]");
		if(linkTarget instanceof ScalableBlockc){
			((ScalableBlockc)linkTarget).setLink(this);
		}
		if(upgradeDatas.isEmpty())throw new IllegalArgumentException("");
	}
	
	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, Pal.accent);
	}

	@Override
	public void setStats(){
		super.setStats();
		stats.add(Stat.output, new TextureFilterValue(linkTarget.icon(Cicon.medium), "Link Target: [accent]" + linkTarget.localizedName + "[]."));
	}

	@Override
	public void setBars() {
		super.setBars();

		bars.add("upgradeProgress",
			(UpgradeBlockBuild entity) -> new Bar(
				() -> Core.bundle.get("ui.remain-time"),
				() -> Color.valueOf("#FF732A"),
				() -> entity.remainTime / entity.costTime()
			)
		);
	}

	@Override
	public void load() {
		super.load();
	}
	
	public class UpgradeBlockBuild extends Building implements Upgraderc{
		public Seq<DataEntity> datas = new Seq<>();

		public int link = -1;
		public int upgradingID = defaultID;
		public int lastestSelectID = -1;
		public float remainTime;
		public float warmup;
		
		protected transient SoundLoop upgradeSoundLoop = new SoundLoop(upgradeSound, 1f);;
		
		protected boolean coreValid(CoreBlock.CoreBuild core) {
			return core != null && core.items != null && !core.items.empty();
		}
		
		protected boolean coreValid() {
			return core() != null && core().items != null && !core().items.empty();
		}
		
		@Override
		public void consumeItems(DataEntity data){
			if(state.rules.infiniteResources)return;
			CoreBlock.CoreBuild core = core();
			if(coreValid(core))core.items.remove(data.requirements());
		}
		
		@Override
		public boolean canUpgrade(DataEntity data) {
			if(data.level >= maxLevel)return false;
			
			if(state.rules.infiniteResources)return true;
			
			CoreBlock.CoreBuild core = core();
			return coreValid() && !isUpgrading() && core.items.has(data.requirements());
		}

		public float costTime(){
			return (upgradingID >= datas.size || upgradingID < 0) ? 0 : datas.get(upgradingID).costTime();
		}
		
		@Override//Data Upgrade
		public void upgradeData(DataEntity data){
			if(!canUpgrade(data))return;
			consumeItems(data);
			remainTime = data.costTime();
		}
		
		//Data Upgrade
		public void upgradeData(int data){
			upgradeData(all().get(data));
			upgradingID = data;
		}
		
		@Override//Updates
		public void updateUpgrading() {
			upgradeSoundLoop = new SoundLoop(upgradeSound, 1f);
			upgradeSoundLoop.update(x, y, true);
			NHSetting.debug(() -> remainTime = -1);
			remainTime -= Time.delta * efficiency();
		}
		
		@Override
		public void completeUpgrade() {
			if(upgradingID < 0 || upgradingID >= datas.size)return;
			upgradeSoundLoop.update(x, y, false);
			upgradeSoundLoop.stop();
			Sounds.unlock.at(this);
			Fx.healBlockFull.at(x, y, block.size, baseColor);
			
			datas.get(upgradingID).upgrade();
			switchAmmo(datas.get(upgradingID));
			
			updateTarget();
			upgradingID = defaultID;
		}
		
		//UI
		protected void buildUpgradeDataTable(Table t) {
			t.pane(table -> datas.each(data -> !data.isUnlocked || (data.type().isLeveled && !data.isMaxLevel()), data -> data.buildTable(table, this))).fillX().growY().row();
		}

		public void switchAmmo(DataEntity data){
			Sounds.click.at(this);
			datas.each(ammo -> ammo.selected = false);
			data.selected = true;
			lastestSelectID = datas.indexOf(data);
			updateTarget();
		}
		
		public void buildSwitchAmmoTable(Table t, boolean setting) {
			t.table(Tex.button, table -> {
				if(setting){
					table.pane(cont -> 
						cont.button("Upgrade", Icon.settings, Styles.cleart, this::upgraderTableBuild).size(TableFuncs.LEN * buttonPerLine, TableFuncs.LEN)
					).fillX().height(TableFuncs.LEN).pad(TableFuncs.OFFSET / 3f).row();
				}
				
				table.pane(cont -> {
					int index = 0;
					for (DataEntity data : datas) {
						if(index % buttonPerLine == 0)cont.row().left();
						cont.button(new TextureRegionDrawable(data.type().icon), Styles.clearPartiali, TableFuncs.LEN, () ->
							switchAmmo(data)
						).size(TableFuncs.LEN).disabled(b ->
							!data.isUnlocked || data.selected
						).left();
						index ++;
					}
				}).fillX().height(TableFuncs.LEN).pad(TableFuncs.OFFSET / 3f);
				if(!setting)table.left();
			}).grow().pad(TableFuncs.OFFSET).row();
		}
		
		@Override
		public int linkPos(){
			return link;
		}
		
		@Override
		public void linkPos(int value) {
			if (linkValid())target().resetUpgrade();
			link = value;
			updateTarget();
		}

		//Overrides

		@Override
		public boolean onConfigureTileTapped(Building other) {
			if (this == other) {
				configure(Tmp.p1.set(-1, -1));
				return false;
			}
			if (other.block != linkTarget)return false;
			if (link == other.pos()) {
				configure(Tmp.p1.set(-1, -1));
				return false;
			} else if (!(other instanceof Scalablec)) {
				ui.showErrorMessage("Failed to connect, target '" + other.toString() + "' doesn't implement @Scalablec");
				return true;
			} else { 
				Scalablec target = (Scalablec)other;
				if (!target.isConnected() && target.team() == team && target.within(this, range())) {
					configure(Point2.unpack(other.pos()));
					return false;
				}
			}
			return true;
		}

		@Override
		public void upgraderTableBuild(){
			BaseDialog dialog = new BaseDialog("Upgrade", Styles.fullDialog);
			dialog.cont.clear();
			dialog.addCloseListener();
			dialog.cont.pane(t -> {
				//
				t.table(Tex.button, table -> {
					table.row().left();
					table.button(
							Icon.infoCircle, Styles.clearPartiali, () -> datas.get(lastestSelectID).showInfo(false, this, core().items)
					).size(TableFuncs.LEN).disabled(b -> lastestSelectID < 0 || datas.isEmpty()).left();

					table.button(Icon.hostSmall, Styles.clearTransi, () ->
							new BaseDialog("All Info") {{
								this.addCloseListener();
								setFillParent(true);
								cont.pane(infos -> datas.each(data -> data.buildTable(infos, UpgradeBlockBuild.this))).fillX().height(TableFuncs.LEN * 5).row();
								cont.button("@back", Icon.left, this::hide).fillX().height(TableFuncs.LEN).pad(TableFuncs.OFFSET / 3);
							}}.show()
					).size(TableFuncs.LEN).left();
					table.button("@back", Icon.left, Styles.cleart, dialog::hide).size(TableFuncs.LEN * 3.5f, TableFuncs.LEN).left().pad(TableFuncs.OFFSET / 3);
				}).left().pad(TableFuncs.OFFSET).row();

				buildSwitchAmmoTable(t, false);

				t.image().pad(TableFuncs.OFFSET).fillX().height(4f).color(Pal.accent).row();
				buildUpgradeDataTable(t);
				t.image().pad(TableFuncs.OFFSET).fillX().height(4f).color(Pal.accent).row();

				t.fill();
			});
			dialog.show();
		}
		
		@Override
		public void updateTile() {
			if(datas == null || datas.isEmpty())setData();
			
			if(remainTime >= 0){
				updateUpgrading();
				if(Mathf.chanceDelta(upgradeEffectChance))for(int i : Mathf.signs)upgradeEffect.at(x + i * Mathf.random(block.size / 2f * tilesize), y - Mathf.random(block.size / 2f * tilesize), block.size / 2f, baseColor);
			}else if(isUpgrading())completeUpgrade();
			
			if(efficiency() > 0 && isUpgrading()){
				if(Mathf.equal(warmup, 1, 0.0015F))warmup = 1f;
				else warmup = Mathf.lerpDelta(warmup, 1, 0.01f);
			}else{
				if(Mathf.equal(warmup, 0, 0.0015F))warmup = 0f;
				else warmup = Mathf.lerpDelta(warmup, 0, 0.03f);
			}
			
			Events.on(EventType.WorldLoadEvent.class, e -> {
				setData();
				updateTarget();
				upgradecGroup.add(this);
			});
		}

		@Override
		public void onDestroyed() {
			super.onDestroyed();
			if(linkValid())target().resetUpgrade();
			upgradecGroup.remove(this);
		}

		@Override
		public void placed() {
			super.placed();
			upgradecGroup.add(this);
			setData();
		}
		
		@Override
		public void drawConfigure() {
			Drawf.dashCircle(x, y, range(), baseColor);

			Draw.color(getLinkColor());
			Lines.square(x, y, block().size * tilesize / 2f + 1.0f);

			drawLink();
			if (linkValid()){
				target().drawConnected();
				target().drawMode();
			}
			Draw.reset();
		}

		@Override
		public void write(Writes write) {
			write.f(this.remainTime);
			write.i(this.link);
			write.i(this.lastestSelectID);
			write.i(this.upgradingID);
			
			datas.each(data -> data.write(write));
		}

		@Override
		public void read(Reads read, byte revision) {
			setData();
			
			this.remainTime = read.f();
			this.link = read.i();
			this.upgradingID = read.i();
			this.lastestSelectID = read.i();

			datas.each(data -> data.read(read, revision));
		}

		@Override
		public void updateTarget() {
			if (linkValid()){
				if(lastestSelectID >= 0 && datas.get(lastestSelectID).isUnlocked)target().setData(datas.get(lastestSelectID));
				target().setLinkPos(pos());
			}
		}
		
		public void setData(){
			for(UpgradeData d : upgradeDatas)datas.add(d.newSubEntity());
		}
		
		@Override//Target confirm
		public boolean linkValid() {
			if (link == -1) return false;
			Building target = world.build(link);
			return target instanceof Scalablec && linkTarget.name.equals(target.block.name) && target.team == team && within(target, range());
		}
		
		public CoreBlock.CoreBuild core(){return this.team.core();}

		@Override public Color getLinkColor(){return baseColor;}
		@Override public boolean isUpgrading(){return upgradingID != defaultID || remainTime >= 0;}
		@Override public float range() { return range; }
		@Override public void buildConfiguration(Table table) {buildSwitchAmmoTable(table, true);}
		@Override public void draw() {
			Draw.rect(region, x, y);
			Draw.z(Layer.bullet);
			new Cube(getLinkColor(), warmup * size / 2.2f, size / 10f).draw(x, y, Mathf.absin(Time.time, 30f, 60f));
		}
		@Override public void onRemoved() {
			upgradecGroup.remove(this);
			if(linkValid())target().resetUpgrade();
		}
		public Scalablec target() {return linkValid() ? (Scalablec)link() : null;}
		
		@Override
		public Seq<DataEntity> all(){
			return datas;
		}
	}
}









