package beinet.cn.utils;

import beinet.cn.utils.dto.GoogleRecaptchaResult;
import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceClient;
import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceSettings;
import com.google.recaptchaenterprise.v1.Assessment;
import com.google.recaptchaenterprise.v1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1.Event;
import com.google.recaptchaenterprise.v1.ProjectName;
import com.google.recaptchaenterprise.v1.RiskAnalysis.ClassificationReason;
import lombok.SneakyThrows;

public class GoogleReCAPTCHAUtil {
    // Your Google Credentials API-Key
    private final static String API_KEY = "https://console.cloud.google.com/apis/credentials 这里生成的API Key";
    // Your Google Cloud Project ID.
    private final static String PROJECT_ID = "reCAPTCHA的项目ID";
    // The reCAPTCHA key associated with the site/app
    private final static String RECAPTCHA_KEY = "reCAPTCHA的key";
    // 前端填写的 data-action="LOGIN_BEINET"
    private final static String RECAPTCHA_ACTION = "LOGIN_BEINET";

    /**
     * Create an assessment to analyze the risk of a UI action.
     *
     * @param token : The generated token obtained from the client.
     */
    @SneakyThrows
    public static GoogleRecaptchaResult createAssessment(String token) {
        String apiKey = API_KEY;
        String projectID = PROJECT_ID;
        String recaptchaKey = RECAPTCHA_KEY;
        String recaptchaAction = RECAPTCHA_ACTION;

        // Create the reCAPTCHA client.
        RecaptchaEnterpriseServiceSettings settings = RecaptchaEnterpriseServiceSettings.newBuilder()
                .setApiKey(apiKey)
                .build();
        // TODO: Cache the client generation code (recommended) or call client.close() before exiting the method.
        try (RecaptchaEnterpriseServiceClient client = RecaptchaEnterpriseServiceClient.create(settings)) {
            // Set the properties of the event to be tracked.
            Event event = Event.newBuilder().setSiteKey(recaptchaKey).setToken(token).build();

            // Build the assessment request.
            CreateAssessmentRequest createAssessmentRequest =
                    CreateAssessmentRequest.newBuilder()
                            .setParent(ProjectName.of(projectID).toString())
                            .setAssessment(Assessment.newBuilder().setEvent(event).build())
                            .build();

            Assessment response = client.createAssessment(createAssessmentRequest);

            // Check if the token is valid.
            if (!response.getTokenProperties().getValid()) {
                String errMsg = "The CreateAssessment call failed because the token was: "
                        + response.getTokenProperties().getInvalidReason().name();
                throw new RuntimeException(errMsg);
            }

            // Check if the expected action was executed.
            if (!response.getTokenProperties().getAction().equals(recaptchaAction)) {
                String errMsg = "The action attribute in reCAPTCHA tag isn't match: "
                        + response.getTokenProperties().getAction();
                throw new RuntimeException(errMsg);
            }

            GoogleRecaptchaResult ret = new GoogleRecaptchaResult();
            // Get the risk score and the reason(s).
            // For more information on interpreting the assessment, see:
            // https://cloud.google.com/recaptcha-enterprise/docs/interpret-assessment
            for (ClassificationReason reason : response.getRiskAnalysis().getReasonsList()) {
                ret.setReason(reason.name() + ";" + ret.getReason());
            }

            float recaptchaScore = response.getRiskAnalysis().getScore();
            ret.setScore(String.valueOf(recaptchaScore));

            // Get the assessment name (id). Use this to annotate the assessment.
            String assessmentName = response.getName();
            ret.setAssessmentName(assessmentName.substring(assessmentName.lastIndexOf("/") + 1));
            return ret;
        }
    }

}