package com.freedrawing.springplus.aws.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.ec2.model.DescribeInstanceStatusRequest
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest

@Service
class Ec2HealthCheckService(
    private val ec2Client: Ec2Client,
    @Value("\${cloud.aws.ec2.instance.id}") private val instanceId: String
) {
    fun checkInstanceHealth(): Map<String, Any> {
        return try {
            val healthInfo = mutableMapOf<String, Any>()
            getInstanceStatus(instanceId, healthInfo)
            getInstanceDetails(instanceId, healthInfo)
            healthInfo
        } catch (e: Exception) {
            mapOf("error" to "Failed to check instance health: ${e.message}")
        }
    }

    private fun getInstanceStatus(instanceId: String, healthInfo: MutableMap<String, Any>) {
        val statusRequest = DescribeInstanceStatusRequest.builder()
            .instanceIds(instanceId)
            .build()

        ec2Client.describeInstanceStatus(statusRequest)
            .instanceStatuses()
            .firstOrNull()
            ?.let { status ->
                healthInfo["instanceStatus"] = status.instanceStatus().status()
                healthInfo["systemStatus"] = status.systemStatus().status()
            } ?: run {
            healthInfo["warning"] = "No status information available"
        }
    }

    private fun getInstanceDetails(instanceId: String, healthInfo: MutableMap<String, Any>) {
        val instancesRequest = DescribeInstancesRequest.builder()
            .instanceIds(instanceId)
            .build()

        ec2Client
            .describeInstances(instancesRequest)
            .reservations()
            .firstOrNull()?.instances()?.firstOrNull()?.let { instance ->
                with(instance) {
                    healthInfo += mapOf(
                        "instanceId" to instanceId(),
                        "instanceType" to instanceType(),
                        "instanceState" to state().name(),
                        "launchTime" to launchTime(),
                        "publicIpAddress" to publicIpAddress(),
                        "privateIpAddress" to privateIpAddress(),
                        // 추가 정보
                        "platform" to platform(),                    // Windows/Linux 등 플랫폼 정보
                        "architecture" to architecture(),            // x86_64, arm64 등 아키텍처
                        "availabilityZone" to placement().availabilityZone(),  // AZ 정보
                        "monitoring" to monitoring().state(),        // 모니터링 상태
                        "tags" to tags().associate { it.key() to it.value() },  // 태그 정보
                        "rootDeviceType" to rootDeviceType(),       // EBS 또는 Instance Store
                        "cpuOptions" to mapOf(                      // CPU 옵션
                            "coreCount" to cpuOptions().coreCount(),
                            "threadsPerCore" to cpuOptions().threadsPerCore()
                        ),
                    )
                }
            } ?: run {
            healthInfo["warning"] = "No instance details available"
        }
    }
}
