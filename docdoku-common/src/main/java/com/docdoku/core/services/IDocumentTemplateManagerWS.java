/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2014 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
 * GNU Affero General Public License for more details.  
 *  
 * You should have received a copy of the GNU Affero General Public License  
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.  
 */
package com.docdoku.core.services;

import com.docdoku.core.common.BinaryResource;
import com.docdoku.core.document.DocumentMasterTemplate;
import com.docdoku.core.document.DocumentMasterTemplateKey;
import com.docdoku.core.exceptions.*;
import com.docdoku.core.meta.InstanceAttributeTemplate;

import javax.jws.WebService;

/**
 *
 * @author Taylor LABEJOF
 */
@WebService
public interface IDocumentTemplateManagerWS {
    String generateId(String pWorkspaceId, String pDocMTemplateId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, DocumentMasterTemplateNotFoundException;

    DocumentMasterTemplate[] getDocumentMasterTemplates(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException;
    DocumentMasterTemplate createDocumentMasterTemplate(String workspaceId, String id, String documentType, String mask, InstanceAttributeTemplate[] attributeTemplates, boolean idGenerated, boolean attributesLocked,String pWorkflowModelId, boolean workflowLocked)
            throws WorkspaceNotFoundException, AccessRightException, DocumentMasterTemplateAlreadyExistsException, UserNotFoundException, NotAllowedException, CreationException, WorkflowModelNotFoundException;
    DocumentMasterTemplate getDocumentMasterTemplate(DocumentMasterTemplateKey pKey) throws WorkspaceNotFoundException, DocumentMasterTemplateNotFoundException, UserNotFoundException, UserNotActiveException;
    DocumentMasterTemplate updateDocumentMasterTemplate(DocumentMasterTemplateKey key, String documentType, String mask, InstanceAttributeTemplate[] attributeTemplates, boolean idGenerated, boolean attributesLocked,String pWorkflowModelId, boolean workflowLocked)
            throws WorkspaceNotFoundException, AccessRightException, DocumentMasterTemplateNotFoundException, UserNotFoundException, WorkflowModelNotFoundException;
    void deleteDocumentMasterTemplate(DocumentMasterTemplateKey key) throws  WorkspaceNotFoundException, AccessRightException, DocumentMasterTemplateNotFoundException, UserNotFoundException;

    long getDiskUsageForDocumentTemplatesInWorkspace(String pWorkspaceId) throws WorkspaceNotFoundException, AccessRightException, AccountNotFoundException;

    BinaryResource saveFileInTemplate(DocumentMasterTemplateKey pDocMTemplateKey, String pName, long pSize) throws WorkspaceNotFoundException, NotAllowedException, DocumentMasterTemplateNotFoundException, FileAlreadyExistsException, UserNotFoundException, UserNotActiveException, CreationException, AccessRightException;
    DocumentMasterTemplate removeFileFromTemplate(String pFullName) throws WorkspaceNotFoundException, DocumentMasterTemplateNotFoundException, AccessRightException, FileNotFoundException, UserNotFoundException, UserNotActiveException;
}