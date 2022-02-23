package pl.julkot1.yes.metadata;

import pl.julkot1.yes.exception.InvalidYesSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetadataBuilder {
    public static List<MetadataObject[]> build(ArrayList<String[]> raw) throws InvalidYesSyntaxException {
        var list = new ArrayList<MetadataObject[]>();
        for (String[] strings : raw) {
            var metadataTemplate = DefaultMetadata.getByToken(strings[0]);
            int index = 0;
            if(metadataTemplate.isPresent()){
                var m = metadataTemplate.get();
                MetadataObject[] obj = new MetadataObject[m.getObjects().length];
                for (int i = 0; i < strings.length; i++){
                    if(index>m.objects.length-1)
                        throw new InvalidYesSyntaxException(0, String.join(" ", strings)+"\nInvalid declaration");
                    if(!strings[i].equals(m.objects[index].getKeyword()))
                        throw new InvalidYesSyntaxException(0, String.join(" ", strings)+"\nInvalid declaration (unknown keyword "+strings[i]+")");
                    var record = m.implement(index);
                    if(i+record.getArgs().length+1>strings.length)
                        throw new InvalidYesSyntaxException(0, String.join(" ", strings)+"\nInvalid declaration");
                    record.setArgs(Arrays.copyOfRange(strings, i+1, i + record.getArgs().length+1));
                    obj[index] =record;
                    index++;
                    i += record.getArgs().length;
                }
               list.add(obj);
            }
        }
        return list;
    }
}
