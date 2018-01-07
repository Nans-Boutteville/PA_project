import java.awt.*;

public class Robot {

    private static final int EENERGIEBASE = 100;

    private Point point;
    private int vie;
    private int energie;

    public Robot()
    {
        this(new Point(0,0));
    }

    public Robot (Point point){
        this.point=point;
        this.vie=100;
        this.energie=this.EENERGIEBASE;
    }

    public Point getCoordonnee(){return this.point;}

    public int getVie(){return this.vie;}

    public int getEnergie(){return this.energie;}

    public void reinitialiseEnergie(){
        this.energie=this.EENERGIEBASE;;
    }

    public void setCoordonnee(Point newCoordonnee){
        this.point=newCoordonnee;
    }

}
