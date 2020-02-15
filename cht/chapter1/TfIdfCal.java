import java.util.Arrays;
import java.util.List;
public class TfIdfCal {
    public double tf(List<String> doc, String term) {
        double termFrequency = 0;
        for (String str : doc) {
            if (str.equalsIgnoreCase(term)) {
                termFrequency++;
            }
        }
        return termFrequency / doc.size();
    }

    public int df(List<List<String>> docs, String term) {
        int n = 0;
        if (term != null && term != "") {
            for (List<String> doc : docs) {
                for (String word : doc) {
                    if (term.equalsIgnoreCase(word)) {
                        n++;
                        break;
                    }
                }
            }
        } else {
            System.out.println("term不能為null或是空字串");
        }
        return n;
    }

    public double idf(List<List<String>> docs, String term) {
        return  Math.log(docs.size()/(double)df(docs,term)+1);
    }

    public double tfIdf(List<String> doc, List<List<String>> 
docs, String term){
        return tf(doc, term) * idf(docs, term);
    }

    public static void main(String[] args) {
       List<String> doc1 = Arrays.asList("人工", "智慧", "成為",
 "網際網路", "大會", "焦點");
       List<String> doc2 = Arrays.asList("Google", "推出", "開放原始碼", 
"人工", "智慧", "系統", "工具");
       List<String> doc3 = Arrays.asList("網際網路", "的", "未來", 
"在", "人工", "智慧");
       List<String> doc4 = Arrays.asList("Google", "開放原始碼", "機器",
 "研讀", "工具");
       List<List<String>> documents = Arrays.asList(doc1, doc2, 
doc3,doc4);
       TfIdfCal calculator = new TfIdfCal();
       System.out.println(calculator.tf(doc2, "Google"));
       System.out.println(calculator.df(documents, "Google"));
       double tfidf = calculator.tfIdf(doc2, documents, "Google");
       System.out.println("TF-IDF (Google) = " + tfidf);
    }
}
