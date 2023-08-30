package com.hamgame.hamgame.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamgame.hamgame.config.TestSecurityConfig;
import com.hamgame.hamgame.config.WithCustomMockUser;
import com.hamgame.hamgame.domain.comment.controller.CommentController;
import com.hamgame.hamgame.domain.comment.dto.CommentSaveRequest;
import com.hamgame.hamgame.domain.comment.service.CommentService;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(value = CommentController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)

	})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class CommentControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	CommentService commentService;

	ObjectMapper objectMapper = new ObjectMapper();

	Long userId = 1L;
	Long gameId = 11L;
	Long boardId = 2L;
	Long commentId = 3L;

	private CommentSaveRequest createSaveRequest() {
		return CommentSaveRequest.builder()
			.comment("new comment")
			.build();
	}

	@Test
	@WithCustomMockUser
	@DisplayName("댓글 작성 - 댓글 내용 없음")
	void testCreateCommentNoComment() throws Exception {
		// Given
		CommentSaveRequest commentSaveRequest = CommentSaveRequest.builder().comment("").build();

		// When & Then
		mockMvc.perform(post("/games/{gameId}/boards/{boardId}/comments", gameId, boardId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(commentSaveRequest)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(commentService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("댓글 작성 - 성공")
	void testCreateComment() throws Exception {
		// Given
		CommentSaveRequest commentSaveRequest = createSaveRequest();
		doNothing().when(commentService).createComment(boardId, commentSaveRequest, userId);

		// When & Then
		mockMvc.perform(post("/games/{gameId}/boards/{boardId}/comments", gameId, boardId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(commentSaveRequest)))
			.andDo(print())
			.andExpect(status().isOk());

		verify(commentService, times(1)).createComment(eq(boardId), any(CommentSaveRequest.class), eq(userId));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("댓글 수정 - 성공")
	void testUpdateComment() throws Exception {
		// Given
		CommentSaveRequest commentSaveRequest = createSaveRequest();
		doNothing().when(commentService).updateComment(boardId, commentId, commentSaveRequest, userId);

		// When & Then
		mockMvc.perform(put("/games/{gameId}/boards/{boardId}/comments/{commentId}", gameId, boardId, commentId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(commentSaveRequest)))
			.andExpect(status().isOk());

		verify(commentService, times(1)).updateComment(eq(boardId), eq(commentId), any(CommentSaveRequest.class),
			eq(userId));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("댓글 삭제 - 성공")
	void testDeleteComment() throws Exception {
		// When & Then
		mockMvc.perform(delete("/games/{gameId}/boards/{boardId}/comments/{commentId}", gameId, boardId, commentId))
			.andExpect(status().isOk());

		verify(commentService, times(1)).deleteComment(eq(boardId), eq(commentId), eq(userId));
	}

}