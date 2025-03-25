package com.ttn.demo.core.models.Impl;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.ttn.demo.core.models.TagComponentModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class, adapters = TagComponentModel.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagComponentModelImpl implements TagComponentModel {

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Page currentPage;    //injected current page for getting tags

    @ValueMapValue
    @Default(values = "Default Namespace")
    private String namespace;

    @ValueMapValue
    @Default(values = "Default Additional Tags")
    private List<String> additionalTags;

    private TagManager tagManager;
    private Set<String> links;

    @PostConstruct
    protected void init() {
        // AdditionalTags list has complete resource Path
        //Iterate through Additional Tags List

        links = new HashSet<>();
        tagManager = request.getResourceResolver().adaptTo(TagManager.class);

        if(additionalTags != null) {
            for(String tag : additionalTags) {
                RangeIterator<Resource> tagIterator = tagManager.find(tag);
                while(tagIterator.hasNext()) {
                    links.add(tagIterator.next().getPath().replace("/jcr:content","") + ".html");
                }
            }
        }

        Tag[] currTags = tagManager.getTags(currentPage.getContentResource());
        if(currTags != null) {
            for(Tag tag : currTags) {
                if(tag.getNamespace().equals(namespace)) {
                    RangeIterator<Resource> tagIterator = tagManager.find(tag.getTagID());
                    while(tagIterator.hasNext()) {
                        links.add(tagIterator.next().getPath().replace("/jcr:content","") + ".html");
                    }
                }
            }
        }
    }
    @Override
    public List<String> getLinks(){
        return new ArrayList<>(links);
    }

}