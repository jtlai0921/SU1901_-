package lucene.file.search.service;

import java.io.IOException;
 
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
 
public class IKTokenizer6x extends Tokenizer{
 
     
    //IK分詞器實現
    private IKSegmenter _IKImplement;
     
    //詞元文字屬性
    private final CharTermAttribute termAtt;
    //詞元位移屬性
    private final OffsetAttribute offsetAtt;
    //詞元分類別屬性（該屬性分類別參考org.wltea.analyzer.core.Lexeme中的分類別常數）
    private final TypeAttribute typeAtt;
    //記錄最後一個詞元的結束位置
    private int endPosition;
     
     
    /**
    public IKTokenizer(Reader in , boolean useSmart){
        super(in);
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input , useSmart);
    }**/
     
    /**
     * Lucene 6.x Tokenizer介面卡類別建構函數
     * 實現最新的Tokenizer接口
     * @param useSmart
     */
    public IKTokenizer6x(boolean useSmart){
        super();
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input , useSmart);
    }
 
    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public boolean incrementToken() throws IOException {
        //清除所有的詞元屬性
        clearAttributes();
        Lexeme nextLexeme = _IKImplement.next();
        if(nextLexeme != null){
            //將Lexeme轉成Attributes
            //設定詞元文字
            termAtt.append(nextLexeme.getLexemeText());
            //設定詞元長度
            termAtt.setLength(nextLexeme.getLength());
            //設定詞元位移
            offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            //記錄分詞的最後位置
            endPosition = nextLexeme.getEndPosition();
            //記錄詞元分類別
            typeAtt.setType(nextLexeme.getLexemeText());          
            //返會true告知還有下個詞元
            return true;
        }
        //返會false告知詞元輸出完畢
        return false;
    }
     
    /*
     * (non-Javadoc)
     * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        _IKImplement.reset(input);
    }   
     
    @Override
    public final void end() {
        // set final offset
        int finalOffset = correctOffset(this.endPosition);
        offsetAtt.setOffset(finalOffset, finalOffset);
    }
}