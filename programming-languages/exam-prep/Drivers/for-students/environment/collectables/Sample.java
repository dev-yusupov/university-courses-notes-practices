package environment.collectables;

public class Sample extends Artefact {
    private boolean tagged;

    public Sample(String locationData, Color color) {
        super(locationData, color);
        tagged = false;
        this.setRigidStructure()
    }
}