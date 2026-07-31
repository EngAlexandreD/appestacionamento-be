package br.com.engalexandre.appestacionamento.dto.pagbank;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagBankOrderRequest(
        @JsonProperty("reference_id") String referenceId,
        PagBankCustomerRequest customer,
        @JsonProperty("qr_codes") List<PagBankQrCodeRequest> qrCodes,
        @JsonProperty("notification_urls") List<String> notificationUrls
) {
}
