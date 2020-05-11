package ojass20.nitjsr.in.ojass.Models;

import java.util.ArrayList;
import java.util.List;

public class PrizeModel1 {
    public Long prize1;
    public Long prize2;
    public Long prize3;
    public Long prize4;
    public Long prize5;
    public Long prize6;
    public Long prizeT;

    public PrizeModel1(Long prize1, Long prize2, Long prize3, Long prize4,Long prize5,Long prize6,Long prizeT){
        this.prize1=prize1;
        this.prize2=prize2;
        this.prize3=prize3;
        this.prize4=prize4;
        this.prize5=prize5;
        this.prize6=prize6;
        this.prizeT=prizeT;

    }
    public PrizeModel1(){}
    public List<Long> getPrizes(){
        List<Long> prizes = new ArrayList<>();
        prizes.add(prize1);
        prizes.add(prize2);
        prizes.add(prize3);
        prizes.add(prize4);
        prizes.add(prize5);
        prizes.add(prize6);
        return prizes;
    }
    public Long getPrize1() {
        return prize1;
    }

    public Long getPrize2() {
        return prize2;
    }

    public Long getPrize3() {
        return prize3;
    }

    public Long getPrizeT() {
        return prizeT;
    }

    public Long getPrize4() {
        return prize4;
    }

    public Long getPrize5() {
        return prize5;
    }

    public Long getPrize6() {
        return prize6;
    }

    public void setPrize1(Long prize1) {
        this.prize1 = prize1;
    }

    public void setPrize2(Long prize2) {
        this.prize2 = prize2;
    }

    public void setPrize3(Long prize3) {
        this.prize3 = prize3;
    }

    public void setPrizeT(Long prizeT) {
        this.prizeT = prizeT;
    }
}
