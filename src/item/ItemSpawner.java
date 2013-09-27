package item;

import entity.Entity;


public class ItemSpawner extends Entity {
    long lastSpawn;
    long nextSpawn;
    int minDelay;
    int maxDelay;


    public enum ItemType {
        HEALTH {
            public void addItem() {
                new HealthItem().addToList();
            }
        },
        SHIELD {
            public void addItem() {
                new ShieldItem().addToList();
            }
        },
        ORBITAL {
            public void addItem() {
                new OrbitalItem().addToList();
            }
        },
        TRIPLESHOT {
            public void addItem() {
                new TripleShotItem().addToList();
            }
        },
        FASTSHOT {
            public void addItem() {
                new FastShotItem().addToList();
            }
        };

        public void addItem() {
            new Item().addToList();
        }
    }

    public ItemSpawner(int min, int max) {
        minDelay = min;
        maxDelay = max;

        lastSpawn = System.currentTimeMillis();
    }

    public void tick(int delta) {
        attemptSpawn();
    }

    public void attemptSpawn() {
        if (System.currentTimeMillis() >= nextSpawn) {
            addRandomItem();
            int delay = (int) (Math.random() * (maxDelay - minDelay)) + minDelay;
            lastSpawn = System.currentTimeMillis();
            nextSpawn = lastSpawn + delay * 1000;
        }
    }

    void addRandomItem() {
        ItemType item = ItemType.values()[(int) (Math.random() * ItemType.values().length)];
        item.addItem();
    }
}
