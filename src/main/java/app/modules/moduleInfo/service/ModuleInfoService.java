package app.modules.moduleInfo.service;


import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.modules.inventory.dto.InventoryDTO;
import app.modules.moduleInfo.repo.ModuleInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ModuleInfoService {

    @Autowired
    private ModuleInfoRepo moduleInfoRepo;

    private final RequestMappingHandlerMapping handlerMapping;
    public ModuleInfoService(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public MsgResponse create(InventoryDTO dto) {

        return null;
    }

    public MsgResponse edit(InventoryDTO dto) {

        return null;
    }

    public MsgResponse delete(Long id) {

        return null;
    }

    public MsgResponse getList(SearchParamDTO dto) {

        return null;
    }


}
