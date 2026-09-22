package no.fintlabs.fintadapterresourcevams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(
    scanBasePackages = [
        "no.fintlabs.fintadapterresourcevams",
        "no.fintlabs.adapter",
    ],
)
@ConfigurationPropertiesScan(
    basePackages = [
        "no.fintlabs.fintadapterresourcevams",
        "no.fintlabs.adapter",
    ],
)
@EnableScheduling
class FintAdapterResourceVamsApplication

fun main(args: Array<String>) {
    runApplication<FintAdapterResourceVamsApplication>(*args)
}
