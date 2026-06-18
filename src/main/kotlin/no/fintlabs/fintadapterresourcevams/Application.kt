package no.fintlabs.fintadapterresourcevams

import no.fintlabs.fintadapterresourcevams.config.VamsClientProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
@ComponentScan(basePackageClasses = [
    VamsClientProperties::class
    ], basePackages = ["no.fintlabs.adapter"]
)
class FintAdapterResourceVamsApplication

fun main(args: Array<String>) {
    runApplication<FintAdapterResourceVamsApplication>(*args)
}
