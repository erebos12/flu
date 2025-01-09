package com.erebos.flu.utils.pojo;

public record Person(String surname, String lastname, int age, int height, int weight) {

    public static class Builder {
        private String surname;
        private String lastname;
        private int age;
        private int height;
        private int weight;

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Person build() {
            return new Person(surname, lastname, age, height, weight);
        }
    }
}
