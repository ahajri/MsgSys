package com.ahajri.btalk.data.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahajri.btalk.data.domain.Discussion;
import com.ahajri.btalk.data.domain.upsert.DiscussionUpsert;
import com.ahajri.btalk.data.repository.DiscussionJsonRepository;
import com.marklogic.client.io.DocumentMetadataHandle;

/**
 * @author <p>
 *         ahajri
 *         </p>
 */
@Service
public class DiscussionService extends AService<Discussion> {

	@Autowired
	DiscussionJsonRepository discussionJsonRepository;

	@Override
	public Discussion create(Discussion model) {
		// Add this document to a dedicated collection for later retrieval
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		Iterator<String> iterator = metadata.getCollections().iterator();
		boolean alreadyExists = false;
		loop: while (iterator.hasNext()) {
			String collectionName = iterator.next();
			if (collectionName.equals(DISCUSSION_COLLECTION)) {
				alreadyExists = true;
				break loop;
			}
		}
		if (!alreadyExists) {
			metadata.getCollections().add(DISCUSSION_COLLECTION);
		}
		model.setStartTime(new Date(System.currentTimeMillis()));
		discussionJsonRepository.persist(model, metadata);
		return findByQuery(model.getId()).get(0);
	}

	@Override
	public Integer remove(Discussion model) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer modifyAll(Discussion query, Discussion update)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer modify(Discussion query, Discussion update, boolean upsert,
			boolean multi) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Discussion> findAll() {
		return discussionJsonRepository.findAll();
	}

	@Override
	public List<Discussion> search(String q) {
		return discussionJsonRepository.searchByExample(q);
	}

	@Override
	public List<Discussion> findByQuery(String q) {
		return discussionJsonRepository.findByQuery(q);
	}

	@Override
	public void replaceInsert(Discussion model, String fragment) {
		discussionJsonRepository.replaceInsert(model, fragment);
	}

	public DiscussionUpsert addMessage(DiscussionUpsert model) throws Exception {
		 List<Discussion> found = discussionJsonRepository.searchByExample("{ \"id\": \""+model.getModel().getId()+"\" }");
		 if (CollectionUtils.isNotEmpty(found)) {
			
		}
		return null;
	}

}