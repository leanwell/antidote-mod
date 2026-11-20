import json
import os

ANTIDOTES = [
    {
        "id": "god_antidote",
        "name": "God Antidote",
        "color": 0xFFDF00,
        "ingredients": ["minecraft:potion", "minecraft:nether_star", "minecraft:totem_of_undying"]
    },
    {
        "id": "blind_antidote",
        "name": "Blindness Antidote",
        "color": 0x1E1E1E,
        "ingredients": ["minecraft:potion", "minecraft:golden_carrot", "minecraft:spider_eye"]
    },
    {
        "id": "color_blind_antidote",
        "name": "Color Blindness Antidote",
        "color": 0x787878,
        "ingredients": ["minecraft:potion", "minecraft:glowstone_dust", "minecraft:ink_sac"]
    },
    {
        "id": "deaf_antidote",
        "name": "Deafness Antidote",
        "color": 0x4A6B82,
        "ingredients": ["minecraft:potion", "minecraft:note_block", "minecraft:white_wool"]
    },
    {
        "id": "diabetes_antidote",
        "name": "Diabetes Antidote",
        "color": 0xE5A65D,
        "ingredients": ["minecraft:potion", "minecraft:honeycomb", "minecraft:apple"]
    },
    {
        "id": "schizophrenia_antidote",
        "name": "Schizophrenia Antidote",
        "color": 0x8A2BE2,
        "ingredients": ["minecraft:potion", "minecraft:gold_nugget", "minecraft:fermented_spider_eye"]
    },
    {
        "id": "mute_antidote",
        "name": "Muteness Antidote",
        "color": 0xE0FFFF,
        "ingredients": ["minecraft:potion", "minecraft:note_block", "minecraft:feather"]
    },
    {
        "id": "seizures_antidote",
        "name": "Seizures Antidote",
        "color": 0xFFA500,
        "ingredients": ["minecraft:potion", "minecraft:honey_bottle", "minecraft:glow_berries"]
    },
    {
        "id": "cancer_antidote",
        "name": "Cancer Antidote",
        "color": 0x228B22,
        "ingredients": ["minecraft:potion", "minecraft:glistering_melon_slice", "minecraft:ghast_tear"]
    },
    {
        "id": "ocd_antidote",
        "name": "OCD Antidote",
        "color": 0x4169E1,
        "ingredients": ["minecraft:potion", "minecraft:clock", "minecraft:compass"]
    },
    {
        "id": "amputated_offhand_antidote",
        "name": "Amputated Offhand Antidote",
        "color": 0xD2691E,
        "ingredients": ["minecraft:potion", "minecraft:shield", "minecraft:slime_ball"]
    },
    {
        "id": "amputated_leg_antidote",
        "name": "Amputated Leg Antidote",
        "color": 0xCD5C5C,
        "ingredients": ["minecraft:potion", "minecraft:leather_boots", "minecraft:slime_ball"]
    },
    {
        "id": "dyslexia_antidote",
        "name": "Dyslexia Antidote",
        "color": 0x20B2AA,
        "ingredients": ["minecraft:potion", "minecraft:book", "minecraft:glow_ink_sac"]
    },
    {
        "id": "short_antidote",
        "name": "Short Stature Antidote",
        "color": 0x8FBC8F,
        "ingredients": ["minecraft:potion", "minecraft:bone_meal", "minecraft:red_mushroom"]
    },
    {
        "id": "dementia_antidote",
        "name": "Dementia Antidote",
        "color": 0x9370DB,
        "ingredients": ["minecraft:potion", "minecraft:chest", "minecraft:book"]
    },
    {
        "id": "asthma_antidote",
        "name": "Asthma Antidote",
        "color": 0x87CEEB,
        "ingredients": ["minecraft:potion", "minecraft:pufferfish", "minecraft:feather"]
    },
    {
        "id": "cerebral_palsy_antidote",
        "name": "Cerebral Palsy Antidote",
        "color": 0xDDA0DD,
        "ingredients": ["minecraft:potion", "minecraft:honey_bottle", "minecraft:slime_ball"]
    },
    {
        "id": "narcolepsy_antidote",
        "name": "Narcolepsy Antidote",
        "color": 0xFF4500,
        "ingredients": ["minecraft:potion", "minecraft:redstone", "minecraft:cocoa_beans"]
    },
    {
        "id": "parkinsons_antidote",
        "name": "Parkinson's Antidote",
        "color": 0xB0C4DE,
        "ingredients": ["minecraft:potion", "minecraft:sweet_berries", "minecraft:iron_ingot"]
    },
    {
        "id": "pollen_allergy_antidote",
        "name": "Pollen Allergy Antidote",
        "color": 0xFFD700,
        "ingredients": ["minecraft:potion", "minecraft:honeycomb", "minecraft:dandelion"]
    },
    {
        "id": "insomnia_antidote",
        "name": "Insomnia Antidote",
        "color": 0x191970,
        "ingredients": ["minecraft:potion", "minecraft:white_wool", "minecraft:sweet_berries"]
    },
    {
        "id": "arthritis_antidote",
        "name": "Arthritis Antidote",
        "color": 0xDC143C,
        "ingredients": ["minecraft:potion", "minecraft:magma_cream", "minecraft:blaze_powder"]
    },
    {
        "id": "brittle_bones_antidote",
        "name": "Brittle Bones Antidote",
        "color": 0xF5F5DC,
        "ingredients": ["minecraft:potion", "minecraft:bone_block", "minecraft:iron_ingot"]
    },
    {
        "id": "adhd_antidote",
        "name": "ADHD Antidote",
        "color": 0x32CD32,
        "ingredients": ["minecraft:potion", "minecraft:copper_ingot", "minecraft:redstone"]
    },
    {
        "id": "tourettes_antidote",
        "name": "Tourette's Antidote",
        "color": 0xFF69B4,
        "ingredients": ["minecraft:potion", "minecraft:note_block", "minecraft:sugar"]
    },
    {
        "id": "vertigo_antidote",
        "name": "Vertigo Antidote",
        "color": 0x40E0D0,
        "ingredients": ["minecraft:potion", "minecraft:feather", "minecraft:compass"]
    },
    {
        "id": "prosopagnosia_antidote",
        "name": "Prosopagnosia Antidote",
        "color": 0xD2B48C,
        "ingredients": ["minecraft:potion", "minecraft:carved_pumpkin", "minecraft:glowstone_dust"]
    },
    {
        "id": "scoliosis_antidote",
        "name": "Scoliosis Antidote",
        "color": 0x556B2F,
        "ingredients": ["minecraft:potion", "minecraft:bamboo", "minecraft:iron_chain"]
    },
    {
        "id": "kleptomania_antidote",
        "name": "Kleptomania Antidote",
        "color": 0xDAA520,
        "ingredients": ["minecraft:potion", "minecraft:chest", "minecraft:gold_ingot"]
    },
    {
        "id": "lactose_intolerance_antidote",
        "name": "Lactose Intolerance Antidote",
        "color": 0xF8F8FF,
        "ingredients": ["minecraft:potion", "minecraft:milk_bucket", "minecraft:apple"]
    },
    {
        "id": "anosognosia_antidote",
        "name": "Anosognosia Antidote",
        "color": 0x708090,
        "ingredients": ["minecraft:potion", "minecraft:spyglass", "minecraft:glowstone_dust"]
    }
]

def to_signed_32(val):
    # Ensure ARGB signed 32-bit int
    argb = 0xFF000000 | val
    if argb >= (1 << 31):
        return argb - (1 << 32)
    return argb

def generate():
    base_res = os.path.join("src", "main", "resources")
    items_dir = os.path.join(base_res, "assets", "antidote", "items")
    models_dir = os.path.join(base_res, "assets", "antidote", "models", "item")
    lang_dir = os.path.join(base_res, "assets", "antidote", "lang")
    recipes_dir = os.path.join(base_res, "data", "antidote", "recipe")

    os.makedirs(items_dir, exist_ok=True)
    os.makedirs(models_dir, exist_ok=True)
    os.makedirs(lang_dir, exist_ok=True)
    os.makedirs(recipes_dir, exist_ok=True)

    # 1. Generate parent model for antidote bottles
    parent_model = {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": "minecraft:item/potion_overlay",
            "layer1": "minecraft:item/potion"
        }
    }
    with open(os.path.join(models_dir, "antidote_bottle.json"), "w", encoding="utf-8") as f:
        json.dump(parent_model, f, indent=2)

    # 2. Generate item definition jsons (with tints)
    translations = {
        "itemGroup.antidote.antidotes": "Antidotes"
    }

    for item in ANTIDOTES:
        item_id = item["id"]
        tint_val = to_signed_32(item["color"])

        # Item definition
        item_def = {
            "model": {
                "type": "minecraft:model",
                "model": "antidote:item/antidote_bottle",
                "tints": [
                    {
                        "type": "minecraft:constant",
                        "value": tint_val
                    }
                ]
            }
        }
        with open(os.path.join(items_dir, f"{item_id}.json"), "w", encoding="utf-8") as f:
            json.dump(item_def, f, indent=2)

        # Translation
        translations[f"item.antidote.{item_id}"] = item["name"]

        # Crafting recipe
        recipe = {
            "type": "minecraft:crafting_shapeless",
            "ingredients": item["ingredients"],
            "result": {
                "id": f"antidote:{item_id}"
            }
        }
        with open(os.path.join(recipes_dir, f"{item_id}.json"), "w", encoding="utf-8") as f:
            json.dump(recipe, f, indent=2)

    # 3. Write language file
    with open(os.path.join(lang_dir, "en_us.json"), "w", encoding="utf-8") as f:
        json.dump(translations, f, indent=2)

    print(f"Successfully generated {len(ANTIDOTES)} item definitions, recipes, and translations.")

if __name__ == "__main__":
    generate()
