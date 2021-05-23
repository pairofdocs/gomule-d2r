package gomule.item;

public class RequirementModifierAccumulator {
    private int levelRequirement;
    private int percentRequirements;

    public void accumulateLevelRequirement(int levelRequirement) {
        this.levelRequirement += levelRequirement;
    }

    public void accumulatePercentRequirements(int percentRequirements) {
        this.percentRequirements += percentRequirements;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public int getPercentRequirements() {
        return percentRequirements;
    }
}
