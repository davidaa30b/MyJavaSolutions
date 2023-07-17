class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words) {

        if(words==null || words.length==0)
            return "";

        if(words.length==1)
            return words[0];


        for(String word :words){
            if(word.equals("")) {
                return "";
            }
        }


        int letterIndex=0,index=0,begin=0;

        int min=words[begin].length();
        for(String word: words){
            if(min>word.length())
                min=word.length();
        }
        char letter=words[begin].charAt(letterIndex);
        while(true){
            if(index==words.length){

                index=0;
                letterIndex++;
                if(letterIndex==min)
                    break;

                letter=words[begin].charAt(letterIndex);
            }
            else if(index< words.length && letter==words[index].charAt(letterIndex)) {
                index++;
            }
            else
                break;
        }
        return words[begin].substring(0,letterIndex);
    }
}