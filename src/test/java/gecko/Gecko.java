package gecko;

import java.util.List;

public class Gecko extends MarkusToy implements Sosun, Spermoglotatel, SpermCargo, Nytik, Petuh {
    public SpermaLiter sperma_in_gecko_ass;
    public final List<Otchim> otchims;
    public final double penisLenght;

    public Gecko () {
        sperma_in_gecko_ass = new SpermaLiter(10000000000000L);
        otchims = List.of(new Otchim("Markus"), new Otchim("Daryl"), new Otchim("Radik"));
        penisLenght = 0.00000001D;
    }

    public SpermaLiter sexWithOtchims(Holes hole) {
        SpermaLiter dinner_for_gecko = new SpermaLiter(0);
        otchims.forEach(otchim -> {
            switch (hole) {
                case ASS -> otchim.doSexInAss(this);
                case EARS -> otchim.doSexInEars(this);
                case EYES -> otchim.doSexInEyes(this);
                case MOUTH -> otchim.doSexInMouth(this);
            }
            dinner_for_gecko.addLiter(otchim.konchat(this));
        });

        return dinner_for_gecko;
    }

    @Override
    public TearLiter nyt_vsem(int mins) {
        otchims.getFirst().doSexInAss(this);
        return new TearLiter(mins * 5L);
    }

    @Override
    public void kukarekat() {

    }

    @Override
    public void kudahtat() {

    }
}
