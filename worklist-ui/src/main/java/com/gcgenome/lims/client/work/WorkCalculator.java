package com.gcgenome.lims.client.work;

import com.gcgenome.lims.api.SequencingApi;
import com.gcgenome.lims.data.Index;
import elemental2.dom.DomGlobal;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;
import net.sayaya.ui.chart.Data;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gcgenome.lims.client.work.WorkModel.*;

@UtilityClass
public class WorkCalculator {
    public Promise<Data[]> initialize(Data[] data, Double dilution, Double volume) {
        for(Data datum: data) {
            datum.delete(Dilution.id).delete(LibraryVolume.id);
            if(dilution!=null)  datum.put(Dilution.id, dilution.toString());
            if(volume!=null)    datum.put(LibraryVolume.id, volume.toString());
        }
        return Promise.resolve(data);
    }
    public Promise<Data[]> calculate(Data[] data) {
        for(Data datum: data) {
            datum.delete(NM.id).delete(Volume.id).delete(TEBuffer.id);
            if(datum.get(ConcQubit.id).isEmpty() || datum.get(FragSize.id).isEmpty()) continue;
            double libConc = Double.parseDouble(datum.get(ConcQubit.id));
            double fragSize = Double.parseDouble(datum.get(FragSize.id));
            double assuming = 650;
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
    public Promise<Data[]> indexing(Data[] data, String plate) {
        return SequencingApi.indices(plate).then(WorkCalculator::mapByAddress).then(map->{
            Map<String, Index> i7 = map.get("i7");
            Map<String, Index> i5 = map.get("i5");
            Arrays.stream(data).forEach(datum->{
                var address = datum.get(Address.id);
                if(i7.containsKey(address)) {
                    var index = i7.get(address);
                    datum.put(IndexI7.id, index.id).put(SequenceI7.id, index.sequence);
                }
                if(i5.containsKey(address)) {
                    var index = i5.get(address);
                    datum.put(IndexI5.id, index.id).put(SequenceI5.id, index.sequence);
                }
            });
            return Promise.resolve(data);
        });
    }
    private Promise<Map<String, Map<String, Index>>> mapByAddress(Index[] indices) {
        return Promise.resolve(Arrays.stream(indices).collect(Collectors.groupingBy(i->i.type.toLowerCase(), Collectors.toMap(i->i.position, i->i))));
    }
}
