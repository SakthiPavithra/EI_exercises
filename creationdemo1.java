class House {
    private String foundation;
    private String structure;
    private String roof;
    private boolean garage;
    private boolean swimmingPool;
    private boolean garden;

    private House(HouseBuilder builder) {
        this.foundation = builder.foundation;
        this.structure = builder.structure;
        this.roof = builder.roof;
        this.garage = builder.garage;
        this.swimmingPool = builder.swimmingPool;
        this.garden = builder.garden;
    }

    @Override
    public String toString() {
        return "House with foundation: " + foundation + ", structure: " + structure + ", roof: " + roof +
               ", Garage: " + garage + ", Swimming Pool: " + swimmingPool + ", Garden: " + garden;
    }

    public static class HouseBuilder {
        private String foundation;
        private String structure;
        private String roof;
        private boolean garage;
        private boolean swimmingPool;
        private boolean garden;

        public HouseBuilder(String foundation, String structure, String roof) {
            this.foundation = foundation;
            this.structure = structure;
            this.roof = roof;
        }

        public HouseBuilder setGarage(boolean hasGarage) {
            this.garage = hasGarage;
            return this;
        }

        public HouseBuilder setSwimmingPool(boolean hasSwimmingPool) {
            this.swimmingPool = hasSwimmingPool;
            return this;
        }

        public HouseBuilder setGarden(boolean hasGarden) {
            this.garden = hasGarden;
            return this;
        }

        public House build() {
            return new House(this);
        }
    }
}

public class creationdemo1 {
    public static void main(String[] args) {
        // Predefined requirements
        String foundation = "Concrete";
        String structure = "Wood";
        String roof = "Tiles";
        boolean garage = true;
        boolean swimmingPool = false;
        boolean garden = true;

        House house = new House.HouseBuilder(foundation, structure, roof)
                            .setGarage(garage)
                            .setSwimmingPool(swimmingPool)
                            .setGarden(garden)
                            .build();

        System.out.println(house);
    }
}
