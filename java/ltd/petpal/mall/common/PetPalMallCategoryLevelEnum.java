package ltd.petpal.mall.common;

/**
 * @author 
 * @apiNote Classification level
 */
public enum PetPalMallCategoryLevelEnum {

    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "level one"),
    LEVEL_TWO(2, "level two"),
    LEVEL_THREE(3, "level three");

    private int level;

    private String name;

    PetPalMallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static PetPalMallCategoryLevelEnum getPetPalMallOrderStatusEnumByLevel(int level) {
        for (PetPalMallCategoryLevelEnum PetPalMallCategoryLevelEnum : PetPalMallCategoryLevelEnum.values()) {
            if (PetPalMallCategoryLevelEnum.getLevel() == level) {
                return PetPalMallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
