package com.mwroblewski.service;

import com.mwroblewski.bean.Attachment;
import com.mwroblewski.model.AttachmentDTO;
import org.springframework.stereotype.Service;

@Service
public class AttachmentDTOService {

    public AttachmentDTO fromAttachmentToAttachmentDTO(Attachment attachment){
        AttachmentDTO attachmentDTO = new AttachmentDTO();

        attachmentDTO.setPhoto(attachment.getPhoto());
        attachmentDTO.setCv(attachment.getCv());
        attachmentDTO.setCoverLetter(attachment.getCoverLetter());

        return attachmentDTO;
    }

    public Attachment fromAttachmentDTOToAttachment(AttachmentDTO attachmentDTO){
        Attachment attachment = new Attachment();

        attachment.setId(attachmentDTO.getId());
        attachment.setPhoto(attachmentDTO.getPhoto());
        attachment.setCv(attachmentDTO.getCv());
        attachment.setCoverLetter(attachmentDTO.getCoverLetter());

        return attachment;
    }


}
