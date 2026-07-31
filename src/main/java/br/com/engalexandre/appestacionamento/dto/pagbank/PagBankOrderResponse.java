package br.com.engalexandre.appestacionamento.dto.pagbank;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PagBankOrderResponse(
        String id,
        @JsonProperty("reference_id") String referenceId,
        @JsonProperty("qr_codes") List<PagBankQrCodeResponse> qrCodes,
        List<PagBankChargeResponse> charges
) {
}
