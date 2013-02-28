package eu.wiss_ki.aapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import lebada.fs.CFS;
import lebada.fs.FSUtil;
import lebada.marker.LegacyMarker;
import lebada.marker.TermOccurence;
import nerimd.annotation.Annotation;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.DefaultText;
import nerimd.annotation.annotators.person.NamePartAnnotator;
import nerimd.annotation.annotators.person.PersonAnnotator;
import nerimd.annotation.annotators.person.ProfessionAnnotator;
import nerimd.annotation.annotators.person.RelationAnnotator;
import nerimd.annotation.annotators.person.TitleAnnotator;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.Token;

public class LegacyPersonMarker extends LegacyMarker {

	Properties personProps = new Properties();
	Properties professionProps = new Properties();
	Properties titleProps = new Properties();
	Properties namepartsProps = new Properties();
	Properties relationProps = new Properties();
	
	PersonAnnotator person;
	ProfessionAnnotator profession;
	NamePartAnnotator namepart;
	RelationAnnotator relation;
	TitleAnnotator title;
	
	
	
	public LegacyPersonMarker(Options options) {
		super(options);

		// defaults
		namepartsProps.setProperty("nerimd.annotation.annotators.person.nameDBFiles", "data/person/namen_alle.xml");
		professionProps.setProperty("nerimd.annotation.annotators.person.professionDBFiles", "data/person/berufe.xml");
		relationProps.setProperty("nerimd.annotation.annotators.person.relationDBFiles", "data/person/personenbeziehungen.xml");
		personProps.setProperty("nerimd.annotation.annotators.person.namePatternDBFiles", "data/person/namensmuster.xml");
		personProps.setProperty("nerimd.annotation.annotators.person.namePartToPersonAttributeNameMappingDBFiles", "data/person/namensmuster.xml");
		personProps.setProperty("nerimd.annotation.annotators.person.PersonAnnotator.skipTokensWithAnnotations", "HUMAN_RELATION PROFESSION SOCIAL_TITLE POINT_IN_TIME TIMESPAN");
		personProps.setProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "nerimd.annotation.annotators.posSets.STTSPOSSet");
		professionProps.setProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "nerimd.annotation.annotators.posSets.STTSPOSSet");
		titleProps.setProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "nerimd.annotation.annotators.posSets.STTSPOSSet");
		relationProps.setProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "nerimd.annotation.annotators.posSets.STTSPOSSet");
		namepartsProps.setProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "nerimd.annotation.annotators.posSets.STTSPOSSet");
		
		Options tmpo = this.options.subset("namepart.");
		for (String k: tmpo.getKeys()) namepartsProps.setProperty(k, tmpo.get(k)); 
		
		tmpo = this.options.subset("person.");
		for (String k: tmpo.getKeys()) personProps.setProperty(k, tmpo.get(k)); 
		
		tmpo = this.options.subset("profession.");
		for (String k: tmpo.getKeys()) professionProps.setProperty(k, tmpo.get(k)); 
		
		tmpo = this.options.subset("relation.");
		for (String k: tmpo.getKeys()) relationProps.setProperty(k, tmpo.get(k)); 
		
		tmpo = this.options.subset("title.");
		for (String k: tmpo.getKeys()) titleProps.setProperty(k, tmpo.get(k)); 
		
		person = new PersonAnnotator(personProps);
		profession = new ProfessionAnnotator(professionProps);
		namepart = new NamePartAnnotator(namepartsProps);
		relation = new RelationAnnotator(relationProps);
		title = new TitleAnnotator(titleProps);
		
	}

	@Override
	protected synchronized TermOccurence[] _markup(DefaultText text, Token[] tokens) {
		
		relation.annotate(text);
		title.annotate(text);
		profession.annotate(text);
		namepart.annotate(text);
		person.annotate(text);

		List<TermOccurence> termOccs = new ArrayList<TermOccurence>();
		
		for (Annotation a: text.getAnnotations(AnnotationType.PERSON)) {

			int dim[] = char2tokenPos(tokens, a.start, a.end);
			if (dim == null) continue;
		
			CFS cfs = FSUtil.newCFS();
			cfs.set("term", a.surface());
			for (Entry<String, String> e: a.attributes.entrySet())
				cfs.set(e.getKey(), e.getValue());
			
			termOccs.add(new TermOccurence(dim[0], dim[1], cfs));
			
		}
		
		return termOccs.toArray(new TermOccurence[termOccs.size()]);
		
	}


}
