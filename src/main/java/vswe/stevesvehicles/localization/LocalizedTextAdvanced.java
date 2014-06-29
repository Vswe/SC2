package vswe.stevesvehicles.localization;


import net.minecraft.util.StatCollector;

public class LocalizedTextAdvanced implements ILocalizedText {
    private String unlocalizedText;

    public LocalizedTextAdvanced(String unlocalizedText) {
        this.unlocalizedText = unlocalizedText;
    }

    @Override
    public String translate(String... params) {
        String result = StatCollector.translateToLocal(unlocalizedText);
        for (int i = 0; i < params.length; i++) {
            String pluralCheck = "[%" + (i + 1) + ":";
            int index = result.indexOf(pluralCheck);
            if (index != -1) {
                int endIndex = result.indexOf("]", index);
                if (endIndex != -1) {
                    String optionsStr = result.substring(index + pluralCheck.length(), endIndex);
                    String options[] = optionsStr.split("\\|");

                    int optionId = (params[i].equals("1") || params[i].equals("-1")) ? 0 : 1;
                    if (optionId >= 0 && optionId < options.length) {
                        String option = options[optionId];
                        result = result.substring(0, index) + option + result.substring(endIndex + 1);

                        //restart
                        i--;
                    }
                }
            }else{
                String listCheck = "[%" + (i + 1) + "->";
                int index2 = result.indexOf(listCheck);
                if (index2 != -1) {
                    int endIndex = result.indexOf("]", index2);
                    if (endIndex != -1) {
                        String optionsStr = result.substring(index2 + listCheck.length(), endIndex);
                        String options[] = optionsStr.split("\\|");

                        int optionId = Integer.parseInt(params[i]);
                        if (optionId >= 0 && optionId < options.length) {
                            String option = options[optionId];
                            result = result.substring(0, index2) + option + result.substring(endIndex + 1);

                            //restart
                            i--;
                        }
                    }
                }else{
                    result = result.replace("[%" + (i + 1) + "]", params[i]);
                }
            }
        }
        return result;
    }
}
