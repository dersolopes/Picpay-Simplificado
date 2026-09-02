package com.dersonlopes.picpaysimplificado.dtos;

import lombok.*;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value, Long payer, Long payee) {


}
