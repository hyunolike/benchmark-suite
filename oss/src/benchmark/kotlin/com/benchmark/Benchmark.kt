package com.benchmark

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.jsoncsvbridge.factory.DefaultCsvCreatorFactory.Companion.generateCsv
import kotlinx.benchmark.*
import kotlinx.benchmark.Benchmark
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Kotlin 클래스는 기본적으로 final 클래스이기 때문에, kotlinx-benchmark에서 벤치마크를 수행하기 위해서는 open 클래스로 선언해야 한다.
 * 여기서 allOpen 어노테이션을 사용하면 모든 클래스에 대해 open 클래스로 선언할 수 있다. (2024.08.15 목)
 */
@State(Scope.Benchmark)
class Benchmark {
    private val outputDir = File("csv_output").apply { mkdirs() }


    /**
     * 비교군. json-csv-bridge-oss
     */
    @Benchmark
    fun testJsonCsvBridgeOSS() {
        val jsonInput = """
        [
            {"name": "Hyunho", "age": 30, "city": "New York"},
            {"name": "Bob", "age": 25, "city": "Los Angeles"},
            {"name": "Charlie", "age": 35, "city": "Chicago"}
        ]
    """

        // JSON to CSV
        // 기능1. JSON 형식 문자열 CSV 파일로 변환
        val jsonOutputPath = outputDir.resolve("oss1_output_json.csv").absolutePath
        val jsonCreator = generateCsv("json")

        jsonCreator.createCsv(jsonInput, jsonOutputPath)
        println("JSON to CSV conversion completed. File saved at: $jsonOutputPath")
    }

    /**
     * 비교군. kotlin-csv-oss
     */
    @Benchmark
    fun testKotlinCsvOSS() {
        val jsonOutputPath = outputDir.resolve("oss2_output_json.csv").absolutePath

        val jsonInput = listOf(listOf("name", "age", "city"), listOf("Hyunho", "30", "New York"), listOf("Bob", "25", "Los Angeles"), listOf("Charlie", "35", "Chicago"))

        csvWriter().open(jsonOutputPath) {
            writeRows(jsonInput)
        }

        println("JSON to CSV conversion completed. File saved at: $jsonOutputPath")
    }

    /**
     * 파일 생성 성능 측정 - json-csv-bridge-oss
     */
    @Benchmark
    fun testFileCreationJsonCsvBridgeOSS() {
        val jsonInput = """
        [
            {"name": "Hyunho", "age": 30, "city": "New York"},
            {"name": "Bob", "age": 25, "city": "Los Angeles"},
            {"name": "Charlie", "age": 35, "city": "Chicago"}
        ]
    """

        val jsonOutputPath = outputDir.resolve("oss1_output_json_file.csv").absolutePath
        val jsonCreator = generateCsv("json")

        val duration = measureTimeMillis {
            jsonCreator.createCsv(jsonInput, jsonOutputPath)
        }

        val fileSize = File(jsonOutputPath).length()
        println("File creation (json-csv-bridge-oss) took: $duration ms. File size: $fileSize bytes.")
    }

    /**
     * 파일 생성 성능 측정 - kotlin-csv-oss
     */
    @Benchmark
    fun testFileCreationKotlinCsvOSS() {
        val jsonOutputPath = outputDir.resolve("oss2_output_json_file.csv").absolutePath

        val jsonInput = listOf(listOf("name", "age", "city"), listOf("Hyunho", "30", "New York"), listOf("Bob", "25", "Los Angeles"), listOf("Charlie", "35", "Chicago"))

        val duration = measureTimeMillis {
            csvWriter().open(jsonOutputPath) {
                writeRows(jsonInput)
            }
        }

        val fileSize = File(jsonOutputPath).length()
        println("File creation (kotlin-csv-oss) took: $duration ms. File size: $fileSize bytes.")
    }
}