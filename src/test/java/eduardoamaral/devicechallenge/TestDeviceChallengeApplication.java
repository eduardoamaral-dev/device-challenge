package eduardoamaral.devicechallenge;

import org.springframework.boot.SpringApplication;

public class TestDeviceChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.from(DeviceChallengeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
