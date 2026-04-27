default: run

run:
    ./gradlew run

build:
    ./gradlew build

clean:
    ./gradlew clean

dist:
    ./gradlew installDist
    @echo "Runnable script: build/install/rts/bin/rts"

test:
    ./gradlew test
