package no.fintlabs.fintadapterresourcevams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class FintAdapterResourceVamsApplication

fun main(args: Array<String>) {
    runApplication<FintAdapterResourceVamsApplication>(*args)
}
