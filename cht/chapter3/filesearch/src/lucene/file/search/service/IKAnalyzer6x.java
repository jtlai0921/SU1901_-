package lucene.file.search.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
public class IKAnalyzer6x extends Analyzer{
    private boolean useSmart;
    public boolean useSmart() {
        return useSmart;
    }
    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }
    /**
     * IK分詞器Lucene  Analyzer接口實現類別
     * 預設細粒度切分算法
     */
    public IKAnalyzer6x(){
        this(false);
    }
    /**
     * IK分詞器Lucene Analyzer接口實現類別
     * @param useSmart 當為true時，分詞器進行智慧切分
     */
    public IKAnalyzer6x(boolean useSmart){
        super();
        this.useSmart = useSmart;
    }   
    /**
     * 重新定義最新版本的createComponents
     * 多載Analyzer接口，建構分詞元件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer _IKTokenizer = new IKTokenizer6x(this.useSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
}