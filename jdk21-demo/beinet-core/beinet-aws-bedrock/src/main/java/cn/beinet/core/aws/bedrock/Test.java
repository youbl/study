package cn.beinet.core.aws.bedrock;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.FoundationModelSummary;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsResponse;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import java.util.List;

/**
 * 参考： <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/java_bedrock_code_examples.html">...</a>
 * 具体代码：<a href="https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/bedrock/src/main/java/com/example/bedrock/sync/ListFoundationModels.java">...</a>
 * @author youbl
 * @since 2025/5/26 20:47
 */
public class Test {
    private final static Region region = Region.of("us-east-1");
    private final static String ak = "aaa";
    private final static String sk = "FT/bbb";

    public static void main(String[] args) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ak, sk);
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);
        BedrockClient client = BedrockClient.builder()
                .credentialsProvider(provider)
                .region(region)
                .build();
        listFoundationModels(client);


        // Create a Bedrock Runtime client in the AWS Region you want to use.
        // Replace the DefaultCredentialsProvider with your preferred credentials provider.
        var converseClient = BedrockRuntimeClient.builder()
                .credentialsProvider(provider)
                .region(region)
                .build();
        ConverseUtil.converse(converseClient);
    }

    public static List<FoundationModelSummary> listFoundationModels(BedrockClient bedrockClient) {
        try {
            ListFoundationModelsResponse response = bedrockClient.listFoundationModels(r -> {
            });

            List<FoundationModelSummary> models = response.modelSummaries();

            if (models.isEmpty()) {
                System.out.println("No available foundation models in " + region.toString());
            } else {
                for (FoundationModelSummary model : models) {
                    System.out.println("Model ID: " + model.modelId());
                    System.out.println("Provider: " + model.providerName());
                    System.out.println("Name:     " + model.modelName());
                    System.out.println();
                }
            }

            return models;

        } catch (SdkClientException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    // snippet-end:[bedrock.java2.list_foundation_models.main]

}
