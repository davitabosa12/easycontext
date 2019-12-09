package smd.ufc.br.easycontext.getters;

import android.location.Location;

import java.util.List;

import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;

public interface LocationGetter {
    List<LocationDefinition> getLocations();
}
