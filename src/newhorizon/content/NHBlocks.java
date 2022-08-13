package newhorizon.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Door;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.liquid.LiquidBridge;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.power.Battery;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.SolidPump;
import mindustry.world.blocks.sandbox.PowerVoid;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.draw.*;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BuildVisibility;
import newhorizon.NewHorizon;
import newhorizon.expand.block.adapt.AdaptUnloader;
import newhorizon.expand.block.adapt.LaserBeamDrill;
import newhorizon.expand.block.defence.ChargeWall;
import newhorizon.expand.block.defence.FireExtinguisher;
import newhorizon.expand.block.defence.ShapedWall;
import newhorizon.expand.block.drawer.DrawFactories;
import newhorizon.expand.block.drawer.DrawPrinter;
import newhorizon.expand.block.special.HyperGenerator;
import newhorizon.expand.block.special.JumpGate;
import newhorizon.expand.block.special.UnitSpawner;

import static mindustry.Vars.tilesize;
import static mindustry.type.ItemStack.with;

public class NHBlocks{

	//Load Mod Factories

	public static Block
		//delivery,
		zetaOre, xenMelter, hyperGenerator, fusionCollapser,
		chargeWall, chargeWallLarge, eoeUpgrader, jumpGate, jumpGateJunior, jumpGatePrimary,
		multiplePresstaniumFactory, presstaniumFactory, seniorProcessorFactory, juniorProcessorFactory, multipleSurgeAlloyFactory,
		zetaFactoryLarge, zetaFactorySmall, fusionEnergyFactory, multipleSteelFactory, irayrondPanelFactory, irayrondPanelFactorySmall,
		setonAlloyFactory, darkEnergyFactory, upgradeSortFactory, metalOxhydrigenFactory, metalOxhydrigenFactoryLarge,
		sandCracker,
		thermoCorePositiveFactory, thermoCoreNegativeFactory, thermoCoreFactory, irdryonVault,
	
		//Turrets
		shockWaveTurret, usualUpgrader, bloodStar, pulseShotgun, beamLaserTurret,
		blaster, endOfEra, thurmix, argmot, thermoTurret, railGun, divlusion,
		blastTurret, empTurret, gravity, multipleLauncher, pulseLaserTurret, multipleArtillery,
		antiMatterTurret, atomSeparator, eternity, synchro,

		//Liquids
		irdryonTank,
		//Liquids factories
		irdryonFluidFactory, xenBetaFactory, xenGammaFactory, zetaFluidFactory, oilRefiner, waterInstancer,
		//walls
		insulatedWall, setonWall, setonWallLarge, heavyDefenceWall, heavyDefenceWallLarge, heavyDefenceDoor, heavyDefenceDoorLarge, laserWall, ancientLaserWall,
		//Distributions
		towardGate, rapidUnloader, liquidAndItemBridge, remoteRouter, multiArmorConveyor, multiConveyor, multiEfficientConveyor,
		multiJunction, multiRouter, multiConduit, multiSteelItemBridge, multiSteelLiquidBridge,
		//Drills
		largeWaterExtractor, beamDrill,
		//Powers
		armorPowerNode, armorBatteryLarge, disposableBattery, radiationGenerator, zetaGenerator, hugeBattery, heavyPowerNode,
		//Defence
		largeMendProjector, shapedWall, assignOverdrive, antiBulletTurret, largeShieldGenerator, fireExtinguisher,
		//Special
		playerJumpGate, debuger, payloadEntrance, gravityTrap, hyperspaceWarper, bombLauncher, scrambler, airRaider, configurer, shieldProjector, unitIniter, remoteStorage,
		disposePowerVoid, disposePowerNode, temporaryPowerSource, gravityTrapSmall, beacon,
	
		//Env
		quantumField, quantumFieldDeep, quantumFieldDisturbing, metalUnit, metalTower, metalGround, metalGroundQuantum,
		metalGroundHeat, conglomerateRock, conglomerateWall
		;
	
	private static void loadEnv(){
		conglomerateWall = new StaticWall("conglomerate-wall"){{
			variants = 4;
			mapColor = Color.valueOf("858585");
		}};
		
		conglomerateRock = new Floor("conglomerate-rock", 3){{
			mapColor = Color.valueOf("565557");
			blendGroup = Blocks.stone;
		}};
		
		metalGroundHeat = new Floor("metal-ground-heat", 3){{
			mapColor = Pal.darkerGray.cpy().lerp(NHColor.darkEnr, 0.5f);
			wall = metalUnit;
			attributes.set(Attribute.water, -1f);
			attributes.set(Attribute.oil, -1f);
			attributes.set(Attribute.heat, 1.25f);
			attributes.set(Attribute.light, 1f);
			attributes.set(Attribute.spores, -1f);
			walkSound = NHSounds.metalWalk;
			walkSoundVolume = 0.05f;
			speedMultiplier = 1.25f;
			
			liquidMultiplier = 0.8f;
			liquidDrop = NHLiquids.quantumLiquid;
			lightColor = NHColor.darkEnrColor;
			emitLight = true;
			lightRadius = 35f;
		}};
		
		quantumField = new Floor("quantum-field", 8){{
			status = NHStatusEffects.quantization;
			statusDuration = 60f;
			speedMultiplier = 1.15f;
			liquidDrop = NHLiquids.quantumLiquid;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			attributes.set(Attribute.light, 2f);
			emitLight = true;
			lightRadius = 32f;
			lightColor = NHColor.darkEnrColor.cpy().lerp(Color.black, 0.1f);
			blendGroup = this;
			
			attributes.set(Attribute.heat, 1.25f);
			attributes.set(Attribute.water, -1f);
			attributes.set(Attribute.oil, -1f);
			attributes.set(Attribute.spores, -1f);
			
//			cacheLayer = NHContent.quantum;
		}};
		
		quantumFieldDeep = new Floor("quantum-field-deep", 0){{
			drownTime = 180f;
			status = NHStatusEffects.quantization;
			statusDuration = 240f;
			speedMultiplier = 1.3f;
			liquidDrop = NHLiquids.quantumLiquid;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			attributes.set(Attribute.light, 3f);
			emitLight = true;
			lightRadius = 40f;
			liquidMultiplier = 2f;
			lightColor = NHColor.darkEnrColor.cpy().lerp(Color.black, 0.2f);
			blendGroup = this;
			
			attributes.set(Attribute.heat, 1.5f);
			attributes.set(Attribute.water, -1f);
			attributes.set(Attribute.oil, -1f);
			attributes.set(Attribute.spores, -1f);
			
//			cacheLayer = NHContent.quantum;
		}};
		
		quantumFieldDisturbing = new Floor("quantum-field-disturbing", 0){{
			drownTime = 180f;
			status = NHStatusEffects.quantization;
			statusDuration = 240f;
			speedMultiplier = 1.3f;
			liquidDrop = NHLiquids.quantumLiquid;
			isLiquid = true;
			attributes.set(Attribute.light, 3f);
			emitLight = true;
			lightRadius = 40f;
			liquidMultiplier = 2f;
			lightColor = NHColor.darkEnrColor.cpy().lerp(Color.white, 0.2f);
			blendGroup = this;
			
			attributes.set(Attribute.heat, 1.5f);
			attributes.set(Attribute.water, -1f);
			attributes.set(Attribute.oil, -1f);
			attributes.set(Attribute.spores, -1f);
			
			cacheLayer = NHContent.quantum;
			
			details = "Has unique shader.";
		}
			
			@Override
			public void load(){
				super.load();
				
				editorIcon = fullIcon = uiIcon = region = Core.atlas.find(NewHorizon.name("quantum-field-disturbing-icon"));
			}
		};
		
		metalUnit = new StaticWall("metal-unit"){{
			variants = 6;
		}};
		
		metalTower = new StaticWall("metal-tower"){{
			variants = 3;
		}};
		
		metalGround = new Floor("metal-ground", 6){{
			mapColor = Pal.darkerGray;
			wall = metalUnit;
			attributes.set(Attribute.water, -1f);
			attributes.set(Attribute.oil, -1f);
			attributes.set(Attribute.heat, 0);
			attributes.set(Attribute.light, 0);
			attributes.set(Attribute.spores, -1f);
			walkSound = NHSounds.metalWalk;
			walkSoundVolume = 0.05f;
			speedMultiplier = 1.25f;
		}};
		
		metalGroundQuantum = new Floor("metal-ground-quantum", 2){{
			mapColor = Pal.darkerMetal;
			wall = metalUnit;
			blendGroup = metalGround;
			attributes.set(Attribute.water, -1f);
			attributes.set(Attribute.oil, -1f);
			attributes.set(Attribute.heat, 0.2f);
			attributes.set(Attribute.light, 0);
			attributes.set(Attribute.spores, -1f);
			walkSound = NHSounds.metalWalk;
			walkSoundVolume = 0.05f;
			speedMultiplier = 1.25f;
			
			emitLight = true;
			lightColor = NHColor.darkEnrColor;
			lightRadius = 4.4f;
		}
			@Override
			public void load(){
				super.load();
				region = Core.atlas.find(NewHorizon.name("metal-ground1"));
			}
		};
	}
	
	private static void loadExperiments(){

	}
	
	private static void loadTurrets(){
	
	}
	
	private static void loadFactories(){
		multiplePresstaniumFactory = new GenericCrafter("multiple-presstanium-factory"){{
			size = 3;
			health = 540;
			requirements(Category.crafting, ItemStack.with(NHItems.presstanium, 80, NHItems.juniorProcessor, 60, Items.thorium, 80));
			craftTime = 60f;
			consumePower(5);
			consumeItems(with(Items.titanium, 6, Items.graphite, 2));
			consumeLiquid(NHLiquids.zetaFluid, 0.125f);
			outputItems = with(NHItems.presstanium, 8, Items.scrap, 1);
			
			itemCapacity = 30;
			liquidCapacity = 30;
			
			drawer = new DrawFactories(){{
				drawRotator = 3f;
				liquidColor = NHLiquids.zetaFluid.color;
			}
				
				@Override
				public void draw(Building entity){
					super.draw(entity);
					if(entity.warmup() > 0f){
						Color flameColor = Color.valueOf("ffc999");
						float lightRadius = 60f, lightAlpha = 0.65f, lightSinScl = 10f, lightSinMag = 5;
						float flameRadius = 3f, flameRadiusIn = 1.9f, flameRadiusScl = 5f, flameRadiusMag = 2f, flameRadiusInMag = 1f;
						
						float g = 0.3f;
						float r = 0.06f;
						float cr = Mathf.random(0.1f);
						
						Draw.z(Layer.block + 0.01f);
						
						Draw.alpha(((1f - g) + Mathf.absin(Time.time, 8f, g) + Mathf.random(r) - r) * entity.warmup());
						
						Draw.tint(flameColor);
						Fill.circle(entity.x, entity.y, flameRadius + Mathf.absin(Time.time, flameRadiusScl, flameRadiusMag) + cr);
						Draw.color(1f, 1f, 1f, entity.warmup());
						Fill.circle(entity.x, entity.y, flameRadiusIn + Mathf.absin(Time.time, flameRadiusScl, flameRadiusInMag) + cr);
						
						Draw.color();
					}
				}
			};
		}};
		
		oilRefiner = new GenericCrafter("oil-refiner"){{
			size = 2;
			requirements(Category.production, ItemStack.with(Items.metaglass, 30, NHItems.juniorProcessor, 20, Items.copper, 60, NHItems.metalOxhydrigen, 45));
			//NHTechTree.add(Blocks.oilExtractor, this);
			health = 200;
			craftTime = 90f;
			liquidCapacity = 60f;
			itemCapacity = 20;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawLiquidRegion(Liquids.oil), new DrawRegion("-top"));
			consumePower(5f);
			consumeItems(new ItemStack(Items.sand, 5));
			outputLiquid = new LiquidStack(Liquids.oil, 15f / 60f);
		}};
		
		waterInstancer = new GenericCrafter("water-instancer"){{
			size = 1;
			updateEffect = Fx.smeltsmoke;
			consumePower(0.5f);
			consumeLiquid(NHLiquids.quantumLiquid, 0.1f);
			outputLiquid = new LiquidStack(Liquids.water, 12f / 60f);
			craftTime = 30f;
			requirements(Category.crafting, BuildVisibility.shown, with(Items.metaglass, 15, Items.copper, 30, NHItems.presstanium, 20));
			//NHTechTree.add(Blocks.mechanicalPump, this);
		}};
		
		xenMelter = new GenericCrafter("xen-melter"){{
			size = 2;
			hasPower = hasLiquids = hasItems = true;
			itemCapacity = 12;
			liquidCapacity = 24;
			craftTime = 60f;
			drawer = new DrawMulti(new DrawDefault(), new DrawLiquidTile(NHLiquids.xenAlpha), new DrawRegion("-top"));
			
			craftEffect = NHFx.lightSkyCircleSplash;
			updateEffect = Fx.smeltsmoke;
			requirements(Category.crafting, BuildVisibility.shown, with(NHItems.juniorProcessor, 35, NHItems.metalOxhydrigen, 50, Items.thorium, 30, NHItems.presstanium, 25));
			consumePower(3f);
			consumeItems(new ItemStack(NHItems.metalOxhydrigen, 4), new ItemStack(NHItems.zeta, 4));
			outputLiquid = new LiquidStack(NHLiquids.xenAlpha, 12f / 60f);
		}};
		
		zetaOre = new OreBlock("ore-zeta"){{
			oreDefault = true;
			variants = 3;
			oreThreshold = 0.95F;
			oreScale = 20.380953F;
			itemDrop = NHItems.zeta;
			localizedName = itemDrop.localizedName;
			mapColor.set(itemDrop.color);
			useColor = true;
		}};
		
		presstaniumFactory = new GenericCrafter("presstanium-factory") {
			{
				requirements(Category.crafting, with(Items.silicon, 45, Items.lead, 115, Items.graphite, 25, Items.titanium, 100));
				hasItems = hasPower = true;
				craftTime = 60f;
				outputItem = new ItemStack(NHItems.presstanium, 2);
				size = 2;
				health = 320;
				craftEffect = Fx.smeltsmoke;
				
				consumePower(3f);
				consumeItems(new ItemStack(Items.titanium, 2), new ItemStack(Items.graphite, 1));
			}
		};
		
		zetaFactorySmall = new GenericCrafter("small-zeta-crystal-factory") {
			{
				requirements(Category.crafting, with(Items.silicon, 15, Items.lead, 30, Items.titanium, 40));
				hasItems = hasPower = true;
				craftTime = 75f;
				outputItem = new ItemStack(NHItems.zeta, 1);
				size = 1;
				health = 60;
				craftEffect = Fx.smeltsmoke;
				updateEffect = Fx.smoke;
				
				consumePower(1.5f);
				consumeItem(Items.thorium, 2);
			}
		};
		
		thermoCorePositiveFactory = new GenericCrafter("thermo-core-positive-factory") {
			{
				requirements(Category.crafting, with(NHItems.seniorProcessor, 15, NHItems.presstanium, 30, Items.titanium, 40));
				hasItems = hasPower = true;
				craftTime = 120f;
				outputItem = new ItemStack(NHItems.thermoCorePositive, 1);
				size = 1;
				health = 60;
				craftEffect = Fx.formsmoke;
				consumePower(3f);
				consumeItem(NHItems.thermoCoreNegative, 1);
			}
		};
		
		thermoCoreNegativeFactory = new GenericCrafter("thermo-core-negative-factory") {
			{
				requirements(Category.crafting, with(NHItems.seniorProcessor, 15, NHItems.presstanium, 30, Items.titanium, 40));
				hasItems = hasPower = true;
				craftTime = 120f;
				outputItem = new ItemStack(NHItems.thermoCoreNegative, 1);
				size = 1;
				health = 60;
				craftEffect = Fx.formsmoke;
				consumePower(3f);
				consumeItem(NHItems.thermoCorePositive, 1);
			}
		};
		
		darkEnergyFactory = new GenericCrafter("dark-energy-factory") {
			{
				requirements(Category.crafting, with(NHItems.irayrondPanel, 60, NHItems.setonAlloy, 30, NHItems.seniorProcessor, 60));
				craftEffect = Fx.smeltsmoke;
				outputItem = new ItemStack(NHItems.darkEnergy, 2);
				craftTime = 90f;
				size = 2;
				hasPower = hasItems = true;
				drawer = new DrawMulti(new DrawDefault(), new DrawFlame(NHItems.darkEnergy.color));
				
				consumeItems(new ItemStack(NHItems.thermoCoreNegative, 1), new ItemStack(NHItems.thermoCorePositive, 1));
				consumePower(20f);
			}
		};
		
		fusionEnergyFactory = new GenericCrafter("fusion-core-energy-factory") {
			{
				requirements(Category.crafting, with(NHItems.juniorProcessor, 60, NHItems.presstanium, 50, Items.thorium, 60, Items.graphite, 30));
				craftEffect = Fx.smeltsmoke;
				outputItem = new ItemStack(NHItems.fusionEnergy, 3);
				craftTime = 90f;
				size = 3;
				itemCapacity = 20;
				liquidCapacity = 60f;
				hasPower = hasLiquids = hasItems = true;
				drawer = new DrawMulti(new DrawDefault(), new DrawFlame(NHItems.fusionEnergy.color));
				consumeLiquid(Liquids.water, 0.3f);
				consumeItems(new ItemStack(NHItems.presstanium, 2), new ItemStack(NHItems.zeta, 6));
				consumePower(6f);
			}
		};
		
		irayrondPanelFactory = new GenericCrafter("irayrond-panel-factory") {
			{
				requirements(Category.crafting, with(NHItems.juniorProcessor, 60, NHItems.presstanium, 50, Items.plastanium, 60, Items.surgeAlloy, 75, Items.graphite, 30));
				craftEffect = new Effect(30f, e -> Angles.randLenVectors(e.id, 7, 4f + e.fin() * 18f, (x, y) -> {
					Draw.color(NHItems.irayrondPanel.color);
					Fill.square(e.x + x, e.y + y, e.fout() * 3f, 45);
				}));
				outputItem = new ItemStack(NHItems.irayrondPanel, 4);
				craftTime = 120f;
				health = 800;
				size = 4;
				hasPower = hasLiquids = hasItems = true;
				drawer = new DrawMulti(new DrawDefault(), new DrawFlame(NHItems.irayrondPanel.color));
				consumeLiquid(NHLiquids.xenAlpha, 0.1f);
				consumeItems(new ItemStack(NHItems.presstanium, 4), new ItemStack(Items.surgeAlloy, 2));
				consumePower(2f);
			}
		};
		
		juniorProcessorFactory = new GenericCrafter("processor-junior-factory") {
			{
				requirements(Category.crafting, with(Items.silicon, 40, NHItems.presstanium, 30, Items.copper, 25, Items.lead, 25));
				craftEffect = Fx.none;
				outputItem = new ItemStack(NHItems.juniorProcessor, 3);
				craftTime = 120f;
				size = 2;
				hasPower = hasItems = true;
				drawer = new DrawMulti(new DrawDefault(), new DrawFlame(NHItems.fusionEnergy.color));
				consumeItems(new ItemStack(Items.silicon, 2), new ItemStack(Items.copper, 4));
				consumePower(2f);
			}
		};
		
		seniorProcessorFactory = new GenericCrafter("processor-senior-factory") {
			{
				requirements(Category.crafting, with(Items.surgeAlloy, 25, NHItems.juniorProcessor, 50, NHItems.presstanium, 25, Items.thorium, 25));
				craftEffect = Fx.none;
				outputItem = new ItemStack(NHItems.seniorProcessor, 4);
				craftTime = 120f;
				size = 2;
				hasPower = hasLiquids = hasItems = true;
				drawer = new DrawMulti(new DrawDefault(), new DrawFlame(NHItems.fusionEnergy.color));
				consumeItems(new ItemStack(Items.surgeAlloy, 2), new ItemStack(NHItems.juniorProcessor, 4));
				consumePower(4f);
			}
		};
		
		irdryonFluidFactory = new GenericCrafter("irdryon-fluid-factory") {
			{
				requirements(Category.crafting, with(Items.surgeAlloy, 20, NHItems.seniorProcessor, 50, NHItems.presstanium, 80, NHItems.irayrondPanel, 65));
				craftEffect = Fx.smeltsmoke;
				outputLiquid = new LiquidStack(NHLiquids.irdryonFluid, 8f / 60f);
				craftTime = 60;
				size = 2;
				drawer = new DrawMulti(new DrawDefault(), new DrawLiquidRegion(NHLiquids.irdryonFluid), new DrawFrames(){{
					sine = true;
				}}, new DrawRegion("-top"));
				itemCapacity = 20;
				hasPower = hasLiquids = hasItems = true;
				consumeLiquid(NHLiquids.xenBeta, 0.075f);
				consumeItems(new ItemStack(NHItems.irayrondPanel, 2), new ItemStack(NHItems.metalOxhydrigen, 4));
				consumePower(4f);
			}
		};
		
		zetaFluidFactory = new GenericCrafter("zeta-fluid-factory") {
			{
				requirements(Category.crafting, with(Items.plastanium, 50, NHItems.juniorProcessor, 35, NHItems.presstanium, 80, Items.graphite, 65));
				craftEffect = Fx.smeltsmoke;
				outputLiquid = new LiquidStack(NHLiquids.zetaFluid, 15f / 60f);
				craftTime = 60f;
				health = 550;
				drawer = new DrawMulti(new DrawDefault(), new DrawFlame(NHLiquids.zetaFluid.color));
				size = 3;
				itemCapacity = 20;
				liquidCapacity = 60f;
				hasPower = hasLiquids = hasItems = true;
				consumeLiquid(Liquids.water, 0.1f);
				consumeItem(NHItems.zeta, 2);
				consumePower(8f);
			}
		};
		
		metalOxhydrigenFactory = new GenericCrafter("metal-oxhydrigen-factory") {
			{
				requirements(Category.crafting, with(Items.copper, 60, NHItems.juniorProcessor, 30, NHItems.presstanium, 25, Items.thorium, 25));
				craftEffect = NHFx.square(NHColor.lightSkyFront, 38, 3, 24, 3.2f);
				outputItem = new ItemStack(NHItems.metalOxhydrigen, 4);
				craftTime = 120f;
				size = 2;
				hasPower = hasLiquids = hasItems = true;
				drawer = new DrawFactories() {
					{
						liquidColor = Liquids.water.color;
						drawRotator = 1.5f;
						drawTop = true;
					}
				};
				consumeLiquid(Liquids.water, 0.1f);
				consumeItem(Items.lead, 2);
				consumePower(2f);
			}
		};
		
		metalOxhydrigenFactoryLarge = new GenericCrafter("metal-oxhydrigen-factory-large"){{
			health = 450;
			size = 3;
			
			requirements(Category.crafting, with(NHItems.multipleSteel, 50, NHItems.juniorProcessor, 60, NHItems.presstanium, 55, NHItems.zeta, 85));
			
			itemCapacity = 40;
			liquidCapacity = 80;
			
			updateEffect = new Effect(40f, 80f, e -> {
				Draw.color(NHColor.lightSkyFront, NHColor.lightSkyBack, e.fin() * 0.8f);
				Lines.stroke(2f * e.fout());
				Lines.spikes(e.x, e.y, 12 * e.finpow(), 1.5f * e.fout() + 4 * e.fslope(), 4, 45);
			});
			craftEffect = NHFx.square(NHColor.lightSkyFront, 38, 5, 34, 5);
			outputItem = new ItemStack(NHItems.metalOxhydrigen, 8);
			craftTime = 60f;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawArcSmelt(){{
				flameRad = 0.45f;
				circleStroke = 0.8f;
				flameRadiusMag = 0.15f;
				flameColor = NHColor.lightSkyFront;
				midColor = NHColor.lightSkyBack;
			}}, new DrawDefault(), new DrawRegion("-top"));
			consumeLiquid(Liquids.water, 0.25f);
			consumeItem(Items.lead, 3);
			consumePower(5f);
		}};
		
		thermoCoreFactory = new GenericCrafter("thermo-core-factory") {
			{
				requirements(Category.crafting, with(NHItems.irayrondPanel, 150, NHItems.seniorProcessor, 80, NHItems.presstanium, 250, Items.plastanium, 80));
				craftEffect = Fx.plasticExplosionFlak;
				outputItem = new ItemStack(NHItems.thermoCorePositive, 4);
				craftTime = 90f;
				itemCapacity = 30;
				health = 1500;
				size = 5;
				hasPower = hasLiquids = hasItems = true;
				drawer = new DrawFactories() {
					{
						liquidColor = NHLiquids.zetaFluid.color;
						drawRotator = 1f;
						drawTop = false;
						pressorSet = new float[] {(craftTime / 6f), -4.2f, 45, 0};
					}
				};
				consumeLiquid(NHLiquids.zetaFluid, 0.2f);
				consumeItems(new ItemStack(NHItems.irayrondPanel, 2), new ItemStack(NHItems.fusionEnergy, 4), new ItemStack(NHItems.metalOxhydrigen, 2));
				consumePower(5f);
			}
		};
		
		upgradeSortFactory = new GenericCrafter("upgradeSort-factory") {{
			requirements(Category.crafting, with(NHItems.setonAlloy, 160, NHItems.seniorProcessor, 80, NHItems.presstanium, 150, Items.thorium, 200));
			updateEffect = NHStatusEffects.quantization.effect;
			craftEffect = new Effect(25f, e -> {
				Draw.color(NHColor.darkEnrColor);
				Angles.randLenVectors(e.id, 4, 24 * e.fout() * e.fout(), (x, y) -> {
					Lines.stroke(e.fout() * 1.7f);
					Lines.square(e.x + x, e.y + y, 2f + e.fout() * 6f);
				});
				
			});
			outputItem = new ItemStack(NHItems.upgradeSort, 3);
			craftTime = 150f;
			size = 3;
			hasPower = hasItems = true;
			drawer = new DrawPrinter(outputItem.item) {{
				printColor = NHColor.darkEnrColor;
				lightColor = Color.valueOf("#E1BAFF");
				moveLength = 4.2f;
				time = 25f;
			}};
			clipSize = size * tilesize * 2f;
			consumeItems(new ItemStack(NHItems.setonAlloy, 4), new ItemStack(NHItems.seniorProcessor, 4));
			consumePower(10f);
		}};
		
		zetaFactoryLarge = new GenericCrafter("large-zeta-factory") {{
			requirements(Category.crafting, with(Items.plastanium, 25, NHItems.juniorProcessor, 50, NHItems.presstanium, 25));
			outputItem = new ItemStack(NHItems.zeta, 3);
			craftTime = 30f;
			size = 2;
			craftEffect = Fx.formsmoke;
			updateEffect = NHFx.trailToGray;
			hasPower = hasItems = hasLiquids = true;
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawCultivator(){{
				bottomColor = NHLiquids.zetaFluid.color.cpy().lerp(Color.gray, 0.1f);
				plantColor = NHLiquids.zetaFluid.color;
				plantColorLight = NHLiquids.zetaFluid.color.cpy().lerp(Color.white, 0.1f);
			}}, new DrawDefault());
			
			consumeItem(Items.thorium, 3);
			consumePower(7f);
			consumeLiquid(Liquids.water, 0.075f);
		}};
		
		multipleSteelFactory = new GenericCrafter("multiple-steel-factory") {
			{
				requirements(Category.crafting, with(Items.graphite, 65, NHItems.juniorProcessor, 65, NHItems.presstanium, 100, Items.metaglass, 30));
				updateEffect = Fx.smeltsmoke;
				craftEffect = Fx.shockwave;
				outputItem = new ItemStack(NHItems.multipleSteel, 4);
				craftTime = 40f;
				itemCapacity = 20;
				health = 600;
				size = 3;
				hasPower = hasItems = true;
				drawer = new DrawFactories() {
					{
						liquidColor = NHLiquids.xenAlpha.color;
						drawTop = false;
						pressorSet = new float[] {(craftTime / 4f), 3.8f, 0, 90};
					}
				};
				consumeLiquid(NHLiquids.xenAlpha, 0.2f);
				consumeItems(new ItemStack(Items.metaglass, 4), new ItemStack(Items.titanium, 2));
				consumePower(3f);
			}
		};
		
		irayrondPanelFactorySmall = new GenericCrafter("small-irayrond-panel-factory"){{
			requirements(Category.crafting, with(NHItems.multipleSteel, 55, NHItems.juniorProcessor, 35, NHItems.presstanium, 60, Items.plastanium, 40, NHItems.zeta, 40));
			craftEffect = new Effect(30f, e -> Angles.randLenVectors(e.id, 6, 3f + e.fin() * 7f, (x, y) -> {
				Draw.color(NHLiquids.xenBeta.color);
				Fill.square(e.x + x, e.y + y, e.fout() * 2f, 45);
			}));
			outputItem = new ItemStack(NHItems.irayrondPanel, 2);
			craftTime = 180f;
			itemCapacity = 24;
			liquidCapacity = 20f;
			health = 500;
			size = 2;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawFactories() {
				{
					liquidColor = NHLiquids.xenAlpha.color;
					drawTop = false;
					pressorSet = new float[] {(craftTime / 8f), 3.8f, 0, 0};
				}
			};
			consumeLiquid(NHLiquids.xenBeta, 0.1f);
			consumeItems(new ItemStack(NHItems.presstanium, 5), new ItemStack(NHItems.metalOxhydrigen, 3));
			consumePower(10f);
		}};
		
		multipleSurgeAlloyFactory = new GenericCrafter("multiple-surge-alloy-factory"){{
			requirements(Category.crafting, BuildVisibility.shown, with(NHItems.irayrondPanel, 80, NHItems.seniorProcessor, 60, Items.plastanium, 40, NHItems.presstanium, 100, Items.surgeAlloy, 40));
			craftEffect = new Effect(30f, e -> Angles.randLenVectors(e.id, 6, 3f + e.fin() * 7f, (x, y) -> {
				Draw.color(Items.surgeAlloy.color);
				Fill.square(e.x + x, e.y + y, e.fout() * 3f, 45);
			}));
			outputItem = new ItemStack(Items.surgeAlloy, 5);
			craftTime = 90f;
			itemCapacity = 30;
			liquidCapacity = 20f;
			health = 500;
			size = 3;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawLiquidRegion(NHLiquids.zetaFluid), new DrawRegion("-top"));
			consumeLiquid(NHLiquids.zetaFluid, 0.1f);
			consumeItems(new ItemStack(NHItems.metalOxhydrigen, 6), new ItemStack(Items.thorium, 6), new ItemStack(NHItems.fusionEnergy, 1));
			consumePower(20f);
		}};
		
		setonAlloyFactory = new GenericCrafter("seton-alloy-factory"){{
			requirements(Category.crafting, with(NHItems.irayrondPanel, 80, NHItems.seniorProcessor, 60, NHItems.presstanium, 100, Items.surgeAlloy, 40));
			craftEffect = new Effect(30f, e -> Angles.randLenVectors(e.id, 6, 4f + e.fin() * 12f, (x, y) -> {
				Draw.color(NHLiquids.irdryonFluid.color);
				Fill.square(e.x + x, e.y + y, e.fout() * 3f);
			}));
			outputItem = new ItemStack(NHItems.setonAlloy, 2);
			craftTime = 60f;
			itemCapacity = 24;
			liquidCapacity = 20f;
			health = 500;
			size = 3;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawFactories() {
				{
					liquidColor = NHLiquids.irdryonFluid.color;
					drawTop = true;
					drawRotator = 1f;
					drawRotator2 = -1.5f;
				}
			};
			consumeLiquid(NHLiquids.irdryonFluid, 0.12f);
			consumeItems(new ItemStack(Items.plastanium, 4), new ItemStack(Items.graphite, 6));
			consumePower(12f);
		}};
		
		xenBetaFactory = new GenericCrafter("xen-beta-factory"){{
			requirements(Category.crafting, with(NHItems.metalOxhydrigen, 35, NHItems.juniorProcessor, 60, Items.plastanium, 20, NHItems.presstanium, 80, Items.metaglass, 40));
			craftEffect = new Effect(30f, e -> Angles.randLenVectors(e.id, 6, 3f + e.fin() * 7f, (x, y) -> {
				Draw.color(NHLiquids.xenBeta.color);
				Fill.square(e.x + x, e.y + y, e.fout() * 2f, 45);
			}));
			outputLiquid = new LiquidStack(NHLiquids.xenBeta, 6f / 60f);
			craftTime = 60f;
			itemCapacity = 12;
			liquidCapacity = 20f;
			health = 260;
			size = 2;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawFactories() {{
				liquidColor = NHLiquids.xenBeta.color;
				drawTop = true;
			}};
			consumeLiquid(NHLiquids.xenAlpha, 0.1f);
			consumeItem(NHItems.zeta, 2);
			consumePower(3f);
		}};
		
		xenGammaFactory = new GenericCrafter("xen-gamma-factory"){{
			requirements(Category.crafting, with(NHItems.irayrondPanel, 70, NHItems.seniorProcessor, 60, Items.surgeAlloy, 20, Items.metaglass, 40));
			craftEffect = new Effect(30f, e -> Angles.randLenVectors(e.id, 6, 3f + e.fin() * 7f, (x, y) -> {
				Draw.color(NHLiquids.xenGamma.color);
				Fill.square(e.x + x, e.y + y, e.fout() * 2f, 45);
			}));
			outputLiquid = new LiquidStack(NHLiquids.xenGamma, 12f / 60f);
			craftTime = 60f;
			itemCapacity = 12;
			liquidCapacity = 20f;
			health = 260;
			size = 2;
			hasPower = hasLiquids = hasItems = true;
			drawer = new DrawFactories() {
				{
					liquidColor = NHLiquids.xenBeta.color;
				}
			};
			consumeLiquid(NHLiquids.xenBeta, 0.2f);
			consumeItems(new ItemStack(Items.phaseFabric, 2));
			consumePower(8f);
		}};
	}
	
	public static void load() {
		final int healthMult2 = 4, healthMult3 = 9;
		
		multiSteelItemBridge = new BufferedItemBridge("multi-steel-item-bridge"){{
			health = 480;
			requirements(Category.distribution, with(NHItems.multipleSteel, 5, NHItems.zeta, 5, Items.graphite, 10));
			
			fadeIn = moveArrows = true;
			hasPower = false;
			range = 8;
			speed = 40;
			arrowSpacing = 8f;
			bufferCapacity = 20;
		}};
		
		multiSteelLiquidBridge = new LiquidBridge("multi-steel-liquid-bridge"){{
			health = 480;
			requirements(Category.liquid, with(NHItems.multipleSteel, 5, NHItems.zeta, 5, Items.metaglass, 10));

			fadeIn = moveArrows = true;
			arrowSpacing = 8;
			range = 8;
			hasPower = false;
		}};
		
		fireExtinguisher = new FireExtinguisher("fire-extinguisher"){{
			size = 3;
			health = 920;
			intensity = 1600;
			
			consumeItem(NHItems.metalOxhydrigen, 2);
			consumePowerCond(3f, FireExtinguisherBuild::isActive);
			
			requirements(Category.defense, with(NHItems.juniorProcessor, 60, NHItems.presstanium, 120, Items.copper, 80, Items.graphite, 60));
//			//NHTechTree.add(Blocks.tsunami, this);
		}};
		
		shapedWall = new ShapedWall("shaped-wall"){{
			health = 6000;
			insulated = absorbLasers = true;
			
			requirements(Category.defense, with(NHItems.upgradeSort, 5, NHItems.juniorProcessor, 2, NHItems.setonAlloy, 10));
		}};
		
		multiConduit = new Conduit("multi-conduit"){{
			size = 1;
			health = 420;
			liquidCapacity = 20.0F;
			liquidPressure = 1.1f;
			leaks = false;
			
			requirements(Category.liquid, with(NHItems.multipleSteel, 1, Items.copper, 2, Items.metaglass, 1));
//			//NHTechTree.add(Blocks.pulseConduit, this);
		}};
		
		multiRouter = new Router("multi-router"){{
			size = 1;
			health = 420;
			speed = 2f;
			
			requirements(Category.distribution, with(NHItems.multipleSteel, 5, NHItems.juniorProcessor, 2, Items.lead, 5));
//			//NHTechTree.add(Blocks.router, this);
		}};
		
		multiJunction = new Junction("multi-junction"){{
			size = 1;
			health = 420;
			speed = 12f;
			capacity = 12;
			
			requirements(Category.distribution, with(NHItems.multipleSteel, 5, NHItems.juniorProcessor, 2, Items.copper, 5));
//			//NHTechTree.add(Blocks.junction, this);
		}};
		
		beamDrill = new LaserBeamDrill("beam-drill"){{
			size = 4;
			health = 960;
			tier = 6;
			drillTime = 150f;
			liquidBoostIntensity = 1.65f;
			warmupSpeed = 0.001f;
			consumePower(6);
			consumeLiquid(Liquids.water, 0.1f).optional(true, true);
			requirements(Category.production, BuildVisibility.shown, with(NHItems.juniorProcessor, 60, NHItems.multipleSteel, 45, NHItems.zeta, 60, NHItems.presstanium, 40, Items.lead, 80));
//			//NHTechTree.add(Blocks.blastDrill, this);
		}};
		
//		remoteStorage = new RemoteCoreStorage("remote-vault"){{
//			size = 3;
//			health = 960;
//			consumePower(10);
//			requirements(Category.effect, BuildVisibility.shown, with(NHItems.irayrondPanel, 200, NHItems.seniorProcessor, 200, NHItems.presstanium, 150, NHItems.multipleSteel, 120));
//			//NHTechTree.add(Blocks.coreShard, this);
//		}};
		
		unitIniter = new UnitSpawner("unit-initer");
		
/*		shieldProjector = new ShieldProjector("shield-projector"){{
			consumePower(1f);
			consumePowerCond(8f, ShieldProjectorBuild::isCharging);
			size = 3;
			itemCapacity = 20;
			consumeItem(NHItems.fusionEnergy, 5);
			requirements(Category.defense, BuildVisibility.shown, with(Items.copper, 300, NHItems.seniorProcessor, 80, NHItems.presstanium, 150, Items.plastanium, 75, NHItems.multipleSteel, 120));
			//NHTechTree.add(Blocks.forceProjector, this);
		}};
		
		scrambler = new AirRaider("scrambler"){{
			requirements(Category.effect, with(NHItems.multipleSteel, 160, NHItems.presstanium, 260, NHItems.seniorProcessor, 100, Items.plastanium, 100, Items.phaseFabric, 150));
			
			range =  720f;
			
			size = 3;
			consumePowerCond(8f, AirRaiderBuild::isCharging);
			consumeItems(with(NHItems.juniorProcessor, 2, Items.phaseFabric, 1, NHItems.metalOxhydrigen, 1));
			itemCapacity = 12;
			burstSpacing = 30f;
			salvos = 2;
			health = 2500;
			
			lightColor = NHColor.thermoPst;
			
			triggeredEffect = new Effect(45f, e -> {
				Draw.color(lightColor);
				Lines.stroke(e.fout() * 2f);
				Lines.square(e.x, e.y, size * tilesize / 2f + tilesize * 1.5f * e.fin(Interp.pow2In));
			});
			
			bulletHitter = new EmpBulletType(){{
				speed = 6f;
				damage = 100f;
				sprite = "missile-large";
				
				status = NHStatusEffects.scrambler;
				statusDuration = 480f;
				
				trailLength = 14;
				trailWidth = 1.5f;
				
				hitColor = trailColor = backColor = lightColor = lightningColor = NHColor.thermoPst;
				frontColor = Color.white;
				
				homingRange = 300f;
				homingDelay = 6f;
				homingPower = 0f; //Custome Homing
				
				hitSound = Sounds.explosionbig;
				trailChance = 0.075f;
				trailEffect = NHFx.polyTrail;
				drawSize = 120f;
				
				hitPowerEffect = applyEffect = NHFx.lightningHitSmall(hitColor);
				
				collides = false;
				scaleVelocity = true;
				hitShake = despawnShake = 16f;
				lightning = 3;
				lightningCone = 360;
				lightningLengthRand = lightningLength = 20;
				shootEffect = NHFx.instShoot(backColor);
				smokeEffect = NHFx.square(backColor, 50f, 3, 80f, 5f);
				shrinkX = shrinkY = 0;
				radius = 100f;
				splashDamageRadius = 60f;
				splashDamage = lightningDamage = damage;
				height = 22f;
				width = 8f;
				lifetime = 120f;
				
				despawnEffect = new Effect(60f, 150f, e -> {
					Draw.color(Color.white, hitColor, 0.3f + e.fin());
					Lines.stroke(2f * e.fout());
					
					float rad = radius * 1.25f * e.fin(Interp.pow4Out);
					Lines.circle(e.x, e.y, rad);
					
					Fill.circle(e.x, e.y, radius / 5f * e.fout());
					Drawf.light(e.x, e.y, rad * 1.2f, hitColor, 0.7f);
				});
				
				hitEffect = new OptionalMultiEffect(NHFx.square(hitColor, 100f, 3, 80f, 8f), NHFx.blast(hitColor, radius));
			}
				
				@Override
				public void update(Bullet b){
					super.update(b);
					
					Teamc target = Units.closestEnemy(b.team, b.x, b.y, homingRange * 2f, Unitc::isPlayer);
					
					if(target != null){
						if(b.within(target, radius / 4))b.time(b.lifetime());
						b.vel.setAngle(Angles.moveToward(b.rotation(), b.angleTo(target), Time.delta * 7));
					}
				}
				
				@Override
				public void despawned(Bullet b){
					super.despawned(b);
					
					Units.nearbyEnemies(b.team, b.x, b.y, radius, u -> {
						if(u.isLocal()) ScreenInterferencer.generate(600);
					});
					
					for(int i = 0; i < 6; i++){
						Vec2 v = Tmp.v6.rnd(radius + Mathf.random(radius)).add(b).cpy();
						NHFx.chainLightningFade.at(b.x, b.y, 12f, hitColor, v);
						Time.run(NHFx.chainLightningFade.lifetime * NHFx.lightningAlign, () -> {
							NHFx.lightningHitSmall.at(v.x, v.y, 20f, hitColor);
						});
					}
				}
			};
		}};
		
		airRaider = new AirRaider("air-raider"){{
			requirements(Category.effect, with(NHItems.upgradeSort, 160, NHItems.presstanium, 260, NHItems.seniorProcessor, 120, NHItems.juniorProcessor, 100, Items.phaseFabric, 150));
			
			size = 3;
			consumePowerCond(6f, AirRaiderBuild::isCharging);
			consumeItem(NHItems.darkEnergy, 2);
			itemCapacity = 16;
			burstSpacing = 15f;
			salvos = 4;
			health = 4500;
			
			triggeredEffect = new Effect(45f, e -> {
				Draw.color(NHColor.darkEnrColor);
				Lines.stroke(e.fout() * 2f);
				Lines.square(e.x, e.y, size * tilesize / 2f + tilesize * 1.5f * e.fin(Interp.pow2In));
			});
			
			bulletHitter = NHBullets.airRaidMissile;
		}};
		
		bombLauncher = new BombLauncher("bomb-launcher"){{
			requirements(Category.effect, with(Items.phaseFabric, 100, NHItems.presstanium, 160, NHItems.juniorProcessor, 100, Items.thorium, 100, Items.surgeAlloy, 75));
			//NHTechTree.add(Blocks.massDriver, this);
			size = 3;
			bulletHitter = new EffectBulletType(75f){{
				trailChance = 0.25f;
				trailEffect = NHFx.trailToGray;
				trailParam = 1.5f;
				
				smokeEffect = NHFx.hugeSmoke;
				shootEffect = NHFx.boolSelector;
				
				collidesTiles = collidesGround = collides = true;
				splashDamage = 500f;
				lightningDamage = 200f;
				hitColor = NHColor.thurmixRed;
				lightning = 3;
				lightningLength = 8;
				lightningLengthRand = 16;
				splashDamageRadius = 120f;
				hitShake = despawnShake = 20f;
				hitSound = despawnSound = Sounds.explosionbig;
				hitEffect = despawnEffect = new OptionalMultiEffect(NHFx.crossBlast(hitColor, splashDamageRadius * 1.25f), NHFx.blast(hitColor, splashDamageRadius * 1.5f));
			}};
			consumePowerCond(6f, BombLauncherBuild::isCharging);
			consumeItem(NHItems.fusionEnergy, 2);
			itemCapacity = 16;
			health = 900;
		}};
		
		hyperspaceWarper = new HyperSpaceWarper("hyper-space-warper"){{
			size = 4;
			health = 2250;
			
			hasPower = hasItems = true;
			itemCapacity = 20;
			consumeItem(NHItems.fusionEnergy, 5);
			consumePower(12f);
			
			requirements(Category.units, BuildVisibility.shown, with(NHItems.irayrondPanel, 200, NHItems.seniorProcessor, 200, NHItems.presstanium, 450, NHItems.zeta, 200));
		}};
		
		gravityTrapSmall = new GravityTrap("gravity-trap-small"){{
			size = 2;
			health = 640;
			range = 16;
			
			consumePower(5f);
			requirements(Category.units, BuildVisibility.shown, with(Items.titanium, 60, NHItems.metalOxhydrigen, 80, NHItems.juniorProcessor, 50, Items.copper, 200, NHItems.zeta, 50));
			//NHTechTree.add(hyperspaceWarper, this);
		}};
		
		gravityTrap = new GravityTrap("gravity-gully"){{
			size = 3;
			health = 1250;
			
			consumePower(8f);
			requirements(Category.units, BuildVisibility.shown, with(Items.plastanium, 80, NHItems.multipleSteel, 80, NHItems.juniorProcessor, 80, Items.copper, 200));
			//NHTechTree.add(gravityTrapSmall, this);
		}};*/
		
		irdryonTank = new LiquidRouter("irdryon-tank"){{
			requirements(Category.liquid, with(NHItems.metalOxhydrigen, 25, NHItems.multipleSteel, 40, Items.metaglass, 25));
//			//NHTechTree.add(Blocks.liquidTank, this);
			size = 3;
			underBullets = true;
			liquidCapacity = 2500.0F;
			health = 2500;
		}};
		
/*		assignOverdrive = new AssignOverdrive("assign-overdrive"){{
			requirements(Category.effect, with(NHItems.irayrondPanel, 120, NHItems.presstanium, 160, NHItems.juniorProcessor, 100, Items.plastanium, 80, Items.surgeAlloy, 75));
			//NHTechTree.add(Blocks.overdriveProjector, this);
			consumePower(14.0F);
			size = 3;
			range = 240.0F;
			speedBoost = 4f;
			useTime = 300.0F;
			hasBoost = true;
			consumeItem(Items.phaseFabric).boost();
			consumeLiquid(NHLiquids.xenBeta, 0.1f);
		}};*/
		
		largeMendProjector = new MendProjector("large-mend-projector"){{
			size = 3;
			reload = 180f;
			useTime = 600f;
			healPercent = 15;
			requirements(Category.effect, with(NHItems.presstanium, 60, NHItems.juniorProcessor, 50, Items.plastanium, 40, Items.thorium, 80));
//			//NHTechTree.add(Blocks.mendProjector, this);
			consumePower(2F);
			range = 160.0F;
			phaseBoost = 12f;
			phaseRangeBoost = 60.0F;
			health = 980;
			consumeItem(NHItems.juniorProcessor).boost();
		}};

		zetaGenerator = new ConsumeGenerator("zeta-generator"){{
			requirements(Category.power,ItemStack.with(NHItems.metalOxhydrigen, 120, NHItems.juniorProcessor, 80, Items.plastanium, 80, NHItems.zeta,100, Items.copper, 150));
//			//NHTechTree.add(Blocks.thoriumReactor,this);
			size = 3;
			powerProduction = 60f;
			ambientSound = Sounds.hum;
			ambientSoundVolume = 0.24F;
			itemCapacity = 30;
			liquidCapacity = 30;
			itemDuration = 150f;
			consumeItem(NHItems.zeta, 3);
			consumeLiquid(Liquids.cryofluid,0.1f);
			
			lightColor = NHItems.zeta.color.cpy().lerp(Color.white, 0.125f);
			generateEffect = NHFx.square(lightColor, 30f, 5, 20f, 4);
//			explodeEffect = NHFx.lightningHitLarge(heatColor);
		}};
		
		hugeBattery = new Battery("huge-battery"){{
			size = 5;
			health = 1600;
			requirements(Category.power, BuildVisibility.shown, ItemStack.with(Items.phaseFabric, 40, NHItems.juniorProcessor, 20, NHItems.zeta, 80, NHItems.presstanium, 35, Items.graphite, 50));
//			//NHTechTree.add(Blocks.batteryLarge, this);
			consumePowerBuffered(500_000);
		}};
		
		armorPowerNode = new PowerNode("armor-power-node"){{
			requirements(Category.power, BuildVisibility.shown, ItemStack.with(NHItems.presstanium, 25, NHItems.juniorProcessor, 5, Items.lead, 25));
//			//NHTechTree.add(Blocks.powerNodeLarge, this);
			size = 2;
			maxNodes = 12;
			laserRange = 8.5F;
			health = 1650;
		}};
		
		heavyPowerNode = new PowerNode("heavy-power-node"){{
			requirements(Category.power, BuildVisibility.shown, ItemStack.with(NHItems.multipleSteel, 25, NHItems.juniorProcessor, 15, NHItems.zeta, 45, NHItems.presstanium, 40));
//			//NHTechTree.add(Blocks.powerNodeLarge, this);
			size = 3;
			maxNodes = 30;
			laserRange = 20F;
			health = 1050;
		}};
		
		largeWaterExtractor = new SolidPump("large-water-extractor"){{
			size = 3;
			pumpAmount = 0.3f;
			requirements(Category.production, ItemStack.with(NHItems.presstanium, 50, NHItems.juniorProcessor, 45, Items.thorium, 60, Items.metaglass, 30));
//			//NHTechTree.add(Blocks.waterExtractor, this);
			result = Liquids.water;
			liquidCapacity = 60.0F;
			rotateSpeed = 1.4F;
			attribute = Attribute.water;
			consumePower(4f);
		}};
		
		rapidUnloader = new AdaptUnloader("rapid-unloader"){{
			speed = 0.5f;
			requirements(Category.effect, BuildVisibility.shown, with(NHItems.presstanium, 20, Items.lead, 15, NHItems.juniorProcessor, 25));
//			//NHTechTree.add(Blocks.unloader, this);
		}};
		
		multiEfficientConveyor = new Conveyor("multi-efficient-conveyor"){{
			requirements(Category.distribution,with(NHItems.zeta, 2,NHItems.multipleSteel, 2));
//			//NHTechTree.add(Blocks.titaniumConveyor, this);
			speed = 0.16f;
			displayedSpeed = 18f;
			health = 120;
			junctionReplacement = multiJunction;
		}};
		
		multiArmorConveyor = new ArmoredConveyor("multi-armor-conveyor"){{
			requirements(Category.distribution,with(NHItems.zeta, 2, NHItems.multipleSteel, 2, Items.thorium, 1));
//			//NHTechTree.add(Blocks.armoredConveyor, this);
			speed = 0.16f;
			displayedSpeed = 18f;
			health =  320;
			junctionReplacement = multiJunction;
		}};

		multiConveyor = new StackConveyor("multi-conveyor"){{
			requirements(Category.distribution,with(NHItems.zeta, 2,NHItems.irayrondPanel, 2, NHItems.juniorProcessor, 1));
//			//NHTechTree.add(Blocks.plastaniumConveyor, this);
			speed = 0.125f;
			health = 320;
			itemCapacity = 20;
			recharge = 1f;
			
			loadEffect = unloadEffect = new Effect(30f, e -> {
				Lines.stroke(1.5f * e.fout(Interp.pow2Out), NHItems.multipleSteel.color);
				Lines.square(e.x, e.y, tilesize / 8f * Mathf.sqrt2 * (e.fin(Interp.pow2Out) * 3 + 1f), 45f);
			});
		}};
		
		hyperGenerator = new HyperGenerator("hyper-generator"){{
			size = 8;
			health = 12500;
			powerProduction = 1250f;
			updateLightning = updateLightningRand = 3;
			effectColor = NHColor.thermoPst;
			itemCapacity = 40;
			itemDuration = 180f;
			ambientSound = Sounds.pulse;
			ambientSoundVolume = 0.1F;
			consumePower(50.0F);
			consumeItems(new ItemStack(NHItems.metalOxhydrigen, 8), new ItemStack(NHItems.thermoCorePositive, 4));
			consumeLiquid(NHLiquids.zetaFluid, 0.25F);
			requirements(Category.power, BuildVisibility.shown, with(NHItems.upgradeSort, 1000, NHItems.setonAlloy, 600, NHItems.irayrondPanel, 400, NHItems.presstanium, 1500, Items.surgeAlloy, 250, Items.metaglass, 250));
//			//NHTechTree.add(fusionCollapser, this);
		}};
		
		insulatedWall = new Wall("insulated-wall"){{
			size = 1;
			health = 300;
			requirements(Category.defense, with(Items.titanium, 10, Items.copper, 5));
			insulated = true;
			absorbLasers = true;
		}};
		
		setonWall = new Wall("seton-wall"){{
			size = 1;
			health = 1250;
			chanceDeflect = 10.0F;
			flashHit = true;
			requirements(Category.defense, with(NHItems.setonAlloy, 5, NHItems.irayrondPanel, 10, Items.silicon, 15, NHItems.presstanium, 15));
		}};
		
		setonWallLarge = new Wall("seton-wall-large"){{
			size = 2;
			health = 1250 * healthMult2;
			chanceDeflect = 10.0F;
			flashHit = true;
			requirements(Category.defense, with(NHItems.setonAlloy, 5 * healthMult2, NHItems.irayrondPanel, 10 * healthMult2, Items.silicon, 15 * healthMult2, NHItems.presstanium, 15 * healthMult2));
		}};
		
		heavyDefenceWall = new Wall("heavy-defence-wall"){{
			size = 1;
			health = 1750;
			absorbLasers = insulated = true;
			requirements(Category.defense, with(NHItems.setonAlloy, 10, NHItems.presstanium, 20));
		}};
		
		heavyDefenceWallLarge = new Wall("heavy-defence-wall-large"){{
			size = 2;
			health = 1750 * healthMult2;
			absorbLasers = insulated = true;
			requirements(Category.defense, with(NHItems.setonAlloy, 10 * healthMult2, NHItems.presstanium, 20 * healthMult2));
		}};
		
		heavyDefenceDoor = new Door("heavy-defence-door"){{
			size = 1;
			health = 1750;
			requirements(Category.defense, with(NHItems.setonAlloy, 10, NHItems.presstanium, 20, NHItems.juniorProcessor, 5));
		}};
		
		heavyDefenceDoorLarge = new Door("heavy-defence-door-large"){{
			size = 2;
			health = 1750 * healthMult2;
			openfx = Fx.dooropenlarge;
			closefx = Fx.doorcloselarge;
			requirements(Category.defense, with(NHItems.setonAlloy, 10 * healthMult2, NHItems.presstanium, 20 * healthMult2, NHItems.juniorProcessor, 5 * healthMult2));
		}};
		
		largeShieldGenerator = new ForceProjector("large-shield-generator") {{
			size = 4;
			radius = 220f;
			shieldHealth = 20000f;
			cooldownNormal = 18f;
			cooldownLiquid = 6f;
			cooldownBrokenBase = 15f;
			consumeItem(NHItems.fusionEnergy).boost();
			phaseUseTime = 180.0F;
			phaseRadiusBoost = 100.0F;
			phaseShieldBoost = 12000.0F;
			consumePower(12F);
			requirements(Category.effect, with(NHItems.seniorProcessor, 120, Items.lead, 250, Items.graphite, 180, NHItems.presstanium, 150, NHItems.fusionEnergy, 80, NHItems.multipleSteel, 50));
		}};
		
		chargeWall = new ChargeWall("charge-wall"){{
			requirements(Category.defense, with(NHItems.irayrondPanel, 10, NHItems.seniorProcessor, 5, NHItems.upgradeSort, 15));
			size = 1;
			absorbLasers = true;
			range = 120;
            health = 1350;
            effectColor = NHColor.lightSkyBack;
		}};
		
		chargeWallLarge = new ChargeWall("charge-wall-large"){{
			requirements(Category.defense, ItemStack.mult(chargeWall.requirements, healthMult2));
			size = 2;
			absorbLasers = true;
			range = 200;
            health = 1350 * healthMult2;
            effectColor = NHColor.lightSkyBack;
		}};
		
		irdryonVault = new StorageBlock("irdryon-vault"){{
            requirements(Category.effect, with(NHItems.presstanium, 150, NHItems.metalOxhydrigen, 50, NHItems.irayrondPanel, 75));
            size = 3;
            health = 3500;
            itemCapacity = 2500;
        }};
		
		jumpGatePrimary = new JumpGate("jump-gate-primary"){{
			size = 3;
			atlasSizeScl = 0.55f;
			squareStroke = 1.75f;
			health = 1800;
			spawnDelay = 90f;
			spawnReloadTime = 750f;
			range = 160f;
			
			itemCapacity = 500;
			
			consumePower(8f);
			
			requirements(Category.units, BuildVisibility.shown, with(
					Items.copper, 250,
					Items.lead, 200,
					Items.titanium, 80,
					Items.silicon, 80
			));
			
			addSets(
				new UnitSet(UnitTypes.poly, new byte[]{NHUnitTypes.OTHERS, 2}, 45 * 60f,
						with(Items.lead, 30, Items.copper, 60, Items.graphite, 45, Items.silicon, 30)
				)
				/*new UnitSet(NHUnitTypes.assaulter, new byte[]{NHUnitTypes.AIR_LINE_2, 1}, 15 * 60f,
					with(Items.silicon, 16, Items.copper, 30, NHItems.zeta, 20)
				),
				new UnitSet(NHUnitTypes.sharp, new byte[]{NHUnitTypes.AIR_LINE_1, 1}, 15 * 60f,
					with(Items.titanium, 30, Items.silicon, 15)
				),
				new UnitSet(NHUnitTypes.branch, new byte[]{NHUnitTypes.AIR_LINE_1, 2}, 30 * 60f,
					with(Items.titanium, 60, Items.silicon, 45, Items.graphite, 30)
				),
				new UnitSet(NHUnitTypes.origin, new byte[]{NHUnitTypes.GROUND_LINE_1, 1}, 20 * 60f,
					with(Items.lead, 15, Items.silicon, 10 ,Items.copper, 10)
				),
				new UnitSet(NHUnitTypes.thynomo, new byte[]{NHUnitTypes.GROUND_LINE_1, 2}, 35 * 60f,
					with(Items.lead, 30, Items.titanium, 60, Items.graphite, 45, Items.silicon, 30)
				),
				new UnitSet(NHUnitTypes.relay, new byte[]{NHUnitTypes.NAVY_LINE_1, 2}, 30 * 60f,
						with(Items.metaglass, 30, Items.titanium, 60, Items.graphite, 30, Items.silicon, 50)
				)*/
			);
		}};
		
		jumpGateJunior = new JumpGate("jump-gate-junior"){{
			size = 5;
			atlasSizeScl = 0.75f;
			squareStroke = 2f;
			health = 6000;
			spawnDelay = 60f;
			spawnReloadTime = 600f;
			range = 300f;
			
			adaptBase = jumpGatePrimary;
			adaptable = true;
			consumePower(25f);
			
			requirements(Category.units, BuildVisibility.shown, with(
					NHItems.presstanium, 800,
					NHItems.metalOxhydrigen, 300,
					NHItems.juniorProcessor, 600,
					Items.plastanium, 350,
					Items.metaglass, 300,
					Items.thorium, 1000
			));
			
			itemCapacity = 1200;
			
			addSets(
				/*new UnitSet(NHUnitTypes.naxos, new byte[]{NHUnitTypes.AIR_LINE_1, 4}, 120 * 60f,
					with(Items.plastanium, 300, NHItems.juniorProcessor, 250, NHItems.presstanium, 500, Items.surgeAlloy, 50, NHItems.metalOxhydrigen, 120)
				),
				new UnitSet(NHUnitTypes.rhino, new byte[]{NHUnitTypes.OTHERS, 3}, 60f * 60f,
					with(Items.lead, 80, Items.graphite, 60, NHItems.presstanium, 60, NHItems.metalOxhydrigen, 60, NHItems.juniorProcessor, 60)
				),*/
				new UnitSet(UnitTypes.mega, new byte[]{NHUnitTypes.OTHERS, 2}, 45 * 60f,
					with(Items.copper, 80, Items.metaglass, 30, NHItems.presstanium, 40, Items.graphite, 40, NHItems.juniorProcessor, 35)
				)/*,
				new UnitSet(NHUnitTypes.gather, new byte[]{NHUnitTypes.OTHERS, 3}, 60f * 60f,
					with(Items.thorium, 80, Items.metaglass, 30, NHItems.presstanium, 80, NHItems.zeta, 120, NHItems.juniorProcessor, 80)
				),
				new UnitSet(NHUnitTypes.aliotiat, new byte[]{NHUnitTypes.GROUND_LINE_1, 3}, 55 * 60f,
					with(Items.copper, 120, NHItems.multipleSteel, 50, NHItems.presstanium, 60, NHItems.juniorProcessor, 45)
				),
				new UnitSet(NHUnitTypes.warper, new byte[]{NHUnitTypes.AIR_LINE_1, 3}, 65 * 60f,
					with(Items.thorium, 90, Items.graphite, 50, NHItems.multipleSteel, 60, NHItems.juniorProcessor, 50)
				),
				new UnitSet(NHUnitTypes.zarkov, new byte[]{NHUnitTypes.NAVY_LINE_1, 4}, 140 * 60f,
						ItemStack.with(NHItems.multipleSteel, 500, NHItems.juniorProcessor, 300, NHItems.presstanium, 400, NHItems.metalOxhydrigen, 200)
				),
				new UnitSet(NHUnitTypes.tarlidor, new byte[]{NHUnitTypes.GROUND_LINE_1, 4}, 130 * 60f,
						ItemStack.with(Items.plastanium, 300, NHItems.juniorProcessor, 250, NHItems.presstanium, 500, NHItems.zeta, 250)
				),
				new UnitSet(NHUnitTypes.striker, new byte[]{NHUnitTypes.AIR_LINE_1, 4}, 150 * 60f,
						ItemStack.with(Items.phaseFabric, 200, NHItems.juniorProcessor, 300, NHItems.presstanium, 350, NHItems.seniorProcessor, 75)
				),
				new UnitSet(NHUnitTypes.ghost, new byte[]{NHUnitTypes.NAVY_LINE_1, 3}, 60 * 60f,
						ItemStack.with(NHItems.presstanium, 60, NHItems.multipleSteel, 50, NHItems.juniorProcessor, 50)
				)*/
			);
		}};
		
		jumpGate = new JumpGate("jump-gate"){{
			consumePower(60f);
			health = 50000;
			spawnDelay = 30f;
			spawnReloadTime = 300f;
			range = 600f;
			squareStroke = 2.35f;
			size = 8;
			adaptable = true;
			adaptBase = jumpGateJunior;
			
			itemCapacity = 3000;
			
			requirements(Category.units, BuildVisibility.shown, with(
				NHItems.presstanium, 1800,
				NHItems.metalOxhydrigen, 800,
				NHItems.seniorProcessor, 800,
				NHItems.multipleSteel, 1000,
				Items.thorium, 2000,
				Items.titanium, 1500,
				Items.phaseFabric, 600,
				NHItems.irayrondPanel, 400
			));
			
			addSets(
				new UnitSet(NHUnitTypes.longinus, new byte[]{NHUnitTypes.AIR_LINE_1, 5}, 400 * 60f,
						with(NHItems.setonAlloy, 300, Items.surgeAlloy, 150, NHItems.seniorProcessor, 400, NHItems.thermoCoreNegative, 200)
				),
				new UnitSet(NHUnitTypes.saviour, new byte[]{NHUnitTypes.OTHERS, 5}, 300 * 60f,
					with(NHItems.setonAlloy, 250, Items.surgeAlloy, 200, NHItems.seniorProcessor, 150, NHItems.thermoCoreNegative, 100, Items.plastanium, 200, NHItems.zeta, 500)
				),
				new UnitSet(UnitTypes.quell, new byte[]{NHUnitTypes.AIR_LINE_2, 4}, 30 * 60f,
					new ItemStack(NHItems.darkEnergy, 500),
					new ItemStack(NHItems.upgradeSort, 500)
				),
				new UnitSet(NHUnitTypes.declining, new byte[]{NHUnitTypes.NAVY_LINE_1, 5}, 420 * 60f,
						with(NHItems.setonAlloy, 500, NHItems.irayrondPanel, 300, NHItems.seniorProcessor, 300, NHItems.thermoCoreNegative, 300)
				),
				new UnitSet(NHUnitTypes.guardian, new byte[]{NHUnitTypes.OTHERS, 5}, 9600f,
						new ItemStack(NHItems.darkEnergy, 1200)
				),
				new UnitSet(NHUnitTypes.sin, new byte[]{NHUnitTypes.GROUND_LINE_1, 6}, 480 * 60f,
						with(NHItems.setonAlloy, 600, NHItems.upgradeSort, 200, NHItems.seniorProcessor, 500, NHItems.thermoCorePositive, 500, NHItems.irayrondPanel, 300)
				),
				new UnitSet(NHUnitTypes.anvil, new byte[]{NHUnitTypes.AIR_LINE_2, 6}, 600 * 60f,
					with(NHItems.zeta, 1000, NHItems.setonAlloy, 500, NHItems.upgradeSort, 200, NHItems.seniorProcessor, 600, NHItems.thermoCorePositive, 400)
				)/*,
				new UnitSet(NHUnitTypes.annihilation, new byte[]{NHUnitTypes.GROUND_LINE_1, 5}, 320 * 60f,
					with(NHItems.setonAlloy, 200, NHItems.irayrondPanel, 500, NHItems.seniorProcessor, 400, NHItems.fusionEnergy, 100)
				),
			
				new UnitSet(NHUnitTypes.hurricane, new byte[]{NHUnitTypes.AIR_LINE_1, 6}, 480 * 60f,
					with(NHItems.setonAlloy, 800, NHItems.upgradeSort, 300, NHItems.seniorProcessor, 800, NHItems.thermoCoreNegative, 500)
				),
				new UnitSet(NHUnitTypes.destruction, new byte[]{NHUnitTypes.AIR_LINE_1, 5}, 360 * 60f,
					with(NHItems.setonAlloy, 300, NHItems.irayrondPanel, 200, NHItems.seniorProcessor, 500, NHItems.fusionEnergy, 150)
				)*/
			);
		}};
		
		loadFactories();
		loadTurrets();
		loadEnv();
		loadExperiments();
		
		disposePowerVoid = new PowerVoid("dispose-power-void"){{
			size = 1;
			rebuildable = false;
			requirements(Category.power, BuildVisibility.sandboxOnly, with());
			alwaysUnlocked = true;
		}};
	}
}
