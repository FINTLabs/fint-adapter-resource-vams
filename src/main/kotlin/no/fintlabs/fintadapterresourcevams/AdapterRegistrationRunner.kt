package no.fintlabs.fintadapterresourcevams

import no.fintlabs.adapter.AdapterRegisterService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

//TODO Await 3.21 in Beta
//@Component
class AdapterRegistrationRunner(
    private val service: AdapterRegisterService
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        service.registerAdapter()
    }
}