package com.freedrawing.springplus.aws.controller

import com.freedrawing.springplus.aws.service.Ec2HealthCheckService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(
    private val ec2HealthCheckService: Ec2HealthCheckService
) {

    @GetMapping("/health/{instanceId}")
    fun getInstanceHealth(@PathVariable instanceId: String): Map<String, Any> {
        return ec2HealthCheckService.checkInstanceHealth(instanceId)
    }
}