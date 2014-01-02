/*

    openid-portal OpenID Authentication/Authorization server for the GL Service
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package org.immunogenomics.gl.oauth.openid.portal.admin;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/admin/**")
@Controller
public class AdminController {

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ImmunogenomicsAuthorizationDao authorizationDetailsDao;

    @RequestMapping(produces = "text/html")
    public String show(ModelMap modelMap, HttpServletRequest request) {
        String itemId = extractPathParam(request);
        if (itemId == null) {
            return list(null, null, modelMap, request);
        } else {
            ImmunogenomicsAuthorization authorization = authorizationDetailsDao.getAuthorization(itemId);
            logger.debug("{} has {}", itemId, authorization);
            modelMap.addAttribute("user", authorization);
            modelMap.addAttribute("itemId", itemId);
            return "admin/show";
        }
    }

    private String extractPathParam(HttpServletRequest request) {
        int index = 2; // email from "/admin/test@gmail.com"
        // Attempting to use Spring's PathVariable failed because email was truncated at "."
        String itemId = null;
        String path = request.getServletPath();
        if (path != null) {
            String[] parts = path.split("/");
            if (parts.length > index) {
                itemId = parts[index];
            }
        }
        return itemId;
    }

    
    @RequestMapping(value="/list", produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, ModelMap uiModel, HttpServletRequest request) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("users", authorizationDetailsDao.subrange(firstResult, sizeNo));
            float nrOfPages = (float) authorizationDetailsDao.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("users", authorizationDetailsDao.subrange(0, 1000));
        }
        return "admin/list";
    }
    
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ImmunogenomicsAuthorization authorizationBean, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorizationBean);
            return "admin/update";
        }
        uiModel.asMap().clear();
        persistAuthorization(authorizationBean);
        return "redirect:/admin/" + encodeUrlPathSegment(authorizationBean.getId(), httpServletRequest);
    }
    
    private void persistAuthorization(ImmunogenomicsAuthorization authorizationBean) {
        authorizationDetailsDao.addOrUpdate(authorizationBean);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String updateForm(HttpServletRequest request, Model uiModel) {
        String itemId = extractPathParam(request);
        ImmunogenomicsAuthorization authorization = authorizationDetailsDao.getAuthorization(itemId);
        logger.debug("{} has {}", itemId, authorization);
        populateEditForm(uiModel, authorization);
        return "admin/update";
    }
    
    void populateEditForm(Model uiModel, ImmunogenomicsAuthorization authorizationBean) {
        uiModel.addAttribute("user", authorizationBean);
    }
    
    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, produces = "text/html")
    public String delete(HttpServletRequest request, Model uiModel) {
        String itemId = extractPathParam(request);
        authorizationDetailsDao.delete(itemId);
        uiModel.asMap().clear();
        return "redirect:/admin";
    }

}
