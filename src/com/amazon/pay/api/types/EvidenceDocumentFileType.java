/**
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.pay.api.types;

/**
 * This enum class represents the evidence document file type.
 * Evidence file type is one of the following:
 * csv, pdf, xls, xlsx, doc, docx, ods, jpg, png.
 * csv represents the CSV file type.
 * pdf represents the PDF file type.
 * xls represents the XLS file type.
 * xlsx represents the XLSX file type.
 * doc represents the DOC file type.
 * docx represents the DOCX file type.
 * ods represents the ODS file type.
 * jpg represents the JPG file type.
 * png represents the PNG file type.
 */

public enum EvidenceDocumentFileType {
    CSV("csv"),
    PDF("pdf"),
    XLS("xls"),
    XLSX("xlsx"),
    DOC("doc"),
    DOCX("docx"),
    ODS("ods"),
    JPG("jpg"),
    PNG("png");

    private final String evidenceDocumentFileType;

    // Constructor
    EvidenceDocumentFileType(String evidenceDocumentFileType) {
        this.evidenceDocumentFileType = evidenceDocumentFileType;
    }

    // Getter method
    public String getEvidenceDocumentFileType() {
        return evidenceDocumentFileType;
    }
}
