package environment.collectables;

import environment.marker.*;

public abstract class Artefact implements Marked {
    protected String locationData;
    protected final Color color;
    private boolean rigidStructure;

    public Artefact(String locationData, Color color) {
        this.locationData = locationData;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public String getLocationData() {
        return this.locationData;
    }

    public boolean getRigidStructure() {
        return this.rigidStructure;
    }

    public void setRigidStructure(boolean rigidStructure) {
        this.rigidStructure = rigidStructure;
    }

    public abstract void extendLocationData(String newData);

    public abstract Artefact retrieve();

    @Override
    public String toString() {
        return "LocationData: " + this.locationData + ", Color: " + color + ", isRigid: " + rigidStructure;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Artefact o = (Artefact) other;

        if (!java.util.Objects.equals(this.color, o.color)) return false;

        if (this.rigidStructure != o.rigidStructure) return false;

        String thisPrefix = this.locationData != null && this.locationData.length() >= 3
            ? this.locationData.substring(0, 3)
            : this.locationData;

        String otherPrefix = o.locationData != null && o.locationData.length() >= 3
            ? o.locationData.substring(0, 3)
            : o.locationData;

        return java.util.Objects.equals(thisPrefix, otherPrefix);
    }

}