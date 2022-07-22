package com.gcgenome.lims.client.work;

import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;
import net.sayaya.ui.chart.Data;

import static com.gcgenome.lims.client.work.WorkModel.*;

@UtilityClass
public class WorkCalculator {
    public Promise<Data[]> initialize(Data[] data, Double assuming, Double dilution, Double volume) {
        for(Data datum: data) {
            datum.delete(Assuming.id).delete(Dilution.id).delete(LibraryVolume.id);
            if(assuming!=null)  datum.put(Assuming.id, assuming.toString());
            if(dilution!=null)  datum.put(Dilution.id, dilution.toString());
            if(volume!=null)    datum.put(LibraryVolume.id, volume.toString());
        }
        return Promise.resolve(data);
    }
    public Promise<Data[]> calculate(Data[] data) {
        for(Data datum: data) {
            datum.delete(NM.id).delete(Volume.id).delete(TEBuffer.id);
            if(datum.get(ConcQubit.id).isEmpty() || datum.get(FragSize.id).isEmpty() || datum.get(Assuming.id).isEmpty()) continue;
            double libConc = Double.parseDouble(datum.get(ConcQubit.id));
            double fragSize = Double.parseDouble(datum.get(FragSize.id));
            double assuming = Double.parseDouble(datum.get(Assuming.id));
            double nm = libConc / (fragSize * assuming) * 1000000;
            datum.put(NM.id, String.valueOf(Math.round(nm*100)/100.0));

            if(datum.get(LibraryVolume.id).isEmpty() || datum.get(Dilution.id).isEmpty()) continue;
            double libVol = Double.parseDouble(datum.get(LibraryVolume.id));
            double nMDil  = Double.parseDouble(datum.get(Dilution.id));
            double totalVol = libVol*(nm/nMDil);
            datum.put(Volume.id, String.valueOf(Math.round(totalVol*10)/10.0));
            double buffer = totalVol-libVol;
            datum.put(TEBuffer.id, String.valueOf(Math.round(buffer*10)/10.0));
        }
        return Promise.resolve(data);
    }
}
