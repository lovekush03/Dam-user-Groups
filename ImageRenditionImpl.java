package com.ttn.demo.core.models.Impl;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetManager;
import com.adobe.granite.asset.api.Rendition;
import com.ttn.demo.core.models.ImageRenditionModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;

@Model(adaptables = Resource.class, adapters = ImageRenditionModel.class)
public class ImageRenditionImpl implements ImageRenditionModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String renditionName;

    @PostConstruct
    public void init() {
        AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
        Asset asset = assetManager.getAsset(image);
        if(asset != null) {
            Rendition rendition = asset.getRendition(renditionName);
            if(rendition != null) {
                image = rendition.getPath();
            }
            else {
                image = asset.getPath();
            }
        }
    }


    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getRenditionName() {
        return renditionName;
    }
}
