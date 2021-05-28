package com.infra.deployment.adapter.deploymentyamlgenerator.yamlrepresenter;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class EmptyRepresenter extends Representer {

    private static final String EMPTY_TAG = "!empty";

    public EmptyRepresenter() {
        this.representers.put(EmptyArray.class, new RepresentEmptyArray());
        this.representers.put(EmptyObject.class, new RepresentEmptyObject());
        this.representers.put(EmptyString.class, new RepresentEmptyString());
    }

    private class RepresentEmptyArray implements Represent {
        public Node representData(Object data) {
            String value = "emptyarray";
            return representScalar(new Tag(EMPTY_TAG), value);
        }
    }

    private class RepresentEmptyObject implements Represent {
        public Node representData(Object data) {
            String value = "emptyobject";
            return representScalar(new Tag(EMPTY_TAG), value);
        }
    }

    private class RepresentEmptyString implements Represent {
        public Node representData(Object data) {
            String value = "emptystring";
            return representScalar(new Tag(EMPTY_TAG), value);
        }
    }
}
