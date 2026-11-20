package com.leanwell.antidote.item;

public enum DisabilityType {
    BLIND("blind", "Blindness", 0x1E1E1E, "Cures permanent blindness"),
    COLOR_BLIND("color_blind", "Color Blindness", 0x787878, "Restores vibrant color vision"),
    DEAF("deaf", "Deafness", 0x4A6B82, "Restores hearing and sound perception"),
    DIABETES("diabetes", "Diabetes", 0xE5A65D, "Balances blood sugar and food tolerance"),
    SCHIZOPHRENIA("schizophrenia", "Schizophrenia", 0x8A2BE2, "Clears hallucinations and mental noise"),
    MUTE("mute", "Muteness", 0xE0FFFF, "Restores voice and speech in chat"),
    SEIZURES("seizures", "Seizures", 0xFFA500, "Stabilizes nervous reflexes and convulsions"),
    CANCER("cancer", "Cancer", 0x228B22, "Halts max health deterioration"),
    OCD("ocd", "OCD", 0x4169E1, "Relieves compulsive urges"),
    AMPUTATED_OFFHAND("amputated_offhand", "Amputated Offhand", 0xD2691E, "Regenerates the offhand limb"),
    AMPUTATED_LEG("amputated_leg", "Amputated Leg", 0xCD5C5C, "Restores natural walking and movement"),
    DYSLEXIA("dyslexia", "Dyslexia", 0x20B2AA, "Clarifies scrambled text and symbols"),
    SHORT("short", "Short Stature", 0x8FBC8F, "Restores standard player height and reach"),
    DEMENTIA("dementia", "Dementia", 0x9370DB, "Prevents inventory scrambling and lost memories"),
    ASTHMA("asthma", "Asthma", 0x87CEEB, "Restores lung capacity and running endurance"),
    CEREBRAL_PALSY("cerebral_palsy", "Cerebral Palsy", 0xDDA0DD, "Smoothes voluntary muscle coordination"),
    NARCOLEPSY("narcolepsy", "Narcolepsy", 0xFF4500, "Prevents sudden sleep episodes and fatigue"),
    PARKINSONS("parkinsons", "Parkinson's", 0xB0C4DE, "Steadies shaky crosshairs and motor control"),
    POLLEN_ALLERGY("pollen_allergy", "Pollen Allergy", 0xFFD700, "Removes fatal allergic reaction to pollen"),
    INSOMNIA("insomnia", "Insomnia", 0x191970, "Restores the natural ability to sleep"),
    ARTHRITIS("arthritis", "Arthritis", 0xDC143C, "Eliminates mining and attack fatigue"),
    BRITTLE_BONES("brittle_bones", "Brittle Bones", 0xF5F5DC, "Reinforces skeletal durability against fall damage"),
    ADHD("adhd", "ADHD", 0x32CD32, "Prevents involuntary hotbar switching"),
    TOURETTES("tourettes", "Tourette's", 0xFF69B4, "Calms involuntary vocal tics and sudden jerks"),
    VERTIGO("vertigo", "Vertigo", 0x40E0D0, "Restores balance and stops world spinning"),
    PROSOPAGNOSIA("prosopagnosia", "Prosopagnosia", 0xD2B48C, "Restores facial recognition of all mobs"),
    SCOLIOSIS("scoliosis", "Scoliosis", 0x556B2F, "Straightens permanently tilted perspective"),
    KLEPTOMANIA("kleptomania", "Kleptomania", 0xDAA520, "Restores the ability to discard and drop items"),
    LACTOSE_INTOLERANCE("lactose_intolerance", "Lactose Intolerance", 0xF8F8FF, "Prevents fatal reaction to dairy and milk"),
    ANOSOGNOSIA("anosognosia", "Anosognosia", 0x708090, "Restores awareness of your active conditions"),
    GOD_ANTIDOTE("god_antidote", "God Antidote", 0xFFDF00, "Cures all disabilities and resets timer");

    private final String id;
    private final String displayName;
    private final int color;
    private final String lore;

    DisabilityType(String id, String displayName, int color, String lore) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.lore = lore;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }

    public String getLore() {
        return lore;
    }
}
