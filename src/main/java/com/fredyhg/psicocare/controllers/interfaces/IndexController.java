package com.fredyhg.psicocare.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;

public interface IndexController {

    @Operation(summary = "Move to documentation", description = "redirect to documentation", tags = "All")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Moved to documentation"),
    })
    void redirectToDocumentation(HttpServletResponse httpServletResponse);
}
