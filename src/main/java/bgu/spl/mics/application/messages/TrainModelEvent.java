package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class TrainModelEvent implements Event<Boolean> {

    private Model model;

    public TrainModelEvent(Model model){
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public int calculateTime(){
        Data data = getModel().getData();
        int size = data.getSize();
        Data.Type type = data.getType();
        if (type == Data.Type.Text)
            return size;
        if  (type == Data.Type.Tabular)
            return size*2;
        return size*4;
    }

}
