package com.hamgame.hamgame.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamgame.hamgame.config.TestSecurityConfig;
import com.hamgame.hamgame.config.WithCustomMockUser;
import com.hamgame.hamgame.domain.board.controller.BoardController;
import com.hamgame.hamgame.domain.board.dto.BoardDto;
import com.hamgame.hamgame.domain.board.dto.BoardListDto;
import com.hamgame.hamgame.domain.board.dto.BoardSaveRequest;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.board.service.BoardService;
import com.hamgame.hamgame.domain.comment.dto.CommentDto;
import com.hamgame.hamgame.domain.user.dto.UserDto;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(value = BoardController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)

	})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class BoardControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BoardService boardService;

	ObjectMapper objectMapper = new ObjectMapper();

	Long userId = 1L;
	Long gameId = 11L;
	Long boardId = 2L;

	BoardSaveRequest getRequest(String title, String content, BoardCategory boardCategory) {
		return BoardSaveRequest.builder()
			.title(title)
			.content(content)
			.boardCategory(boardCategory)
			.build();
	}

	List<BoardListDto> getBoardListDto() {
		List<BoardListDto> boards = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			boards.add(
				BoardListDto.builder()
					.boardId((long)i)
					.title("title" + i)
					.content("content" + i)
					.image("image" + i)
					.viewCount(i)
					.boardCategory(BoardCategory.DEFAULT)
					.nickname("nickname" + i)
					.commentCount(i)
					.build()
			);
		}
		return boards;
	}

	List<CommentDto> getCommentsDto() {
		List<CommentDto> comments = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			comments.add(
				CommentDto.builder()
					.commentId((long)i)
					.comment("comment" + i)
					.userDto(
						UserDto.builder()
							.id((long)i)
							.email("email" + i)
							.nickname("nickname" + i)
							.build()
					).build()
			);
		}
		return comments;
	}

	@Test
	@DisplayName("게시판 조회 - 성공")
	void getBoardList() throws Exception {
		// Given
		List<BoardListDto> boards = getBoardListDto();
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "boardId");
		Page<BoardListDto> pageResult = new PageImpl<>(boards, pageable, boards.size());

		when(boardService.getBoardList(gameId, pageable, null)).thenReturn(pageResult);

		// When & Then
		mockMvc.perform(get("/games/{gameId}/boards", gameId)
				.param("page", "0")
				.param("size", "10")
				.param("category", "")
			).andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.number").value(0))
			.andExpect(jsonPath("$.size").value(10))
			.andExpect(jsonPath("$.content.[*].boardId", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.content.[*].title", containsInAnyOrder("title1", "title2", "title3")))
			.andExpect(jsonPath("$.content.[*].content", containsInAnyOrder("content1", "content2", "content3")))
			.andExpect(jsonPath("$.content.[*].image", containsInAnyOrder("image1", "image2", "image3")))
			.andExpect(jsonPath("$.content.[*].viewCount", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.content.[*].boardCategory", containsInAnyOrder("DEFAULT", "DEFAULT", "DEFAULT")))
			.andExpect(jsonPath("$.content.[*].nickname", containsInAnyOrder("nickname1", "nickname2", "nickname3")))
			.andExpect(jsonPath("$.content.[*].commentCount", containsInAnyOrder(1, 2, 3)));

		verify(boardService, times(1)).getBoardList(anyLong(), eq(pageable), any());
	}

	@Test
	@DisplayName("게시판 카테고리별 조회 - 성공")
	void getBoardListWithCategory() throws Exception {
		// Given
		List<BoardListDto> boards = getBoardListDto();
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "boardId");
		Page<BoardListDto> pageResult = new PageImpl<>(boards, pageable, boards.size());

		when(boardService.getBoardList(gameId, pageable, BoardCategory.DEFAULT)).thenReturn(pageResult);

		// When & Then
		mockMvc.perform(get("/games/{gameId}/boards", gameId)
				.param("page", "0")
				.param("size", "10")
				.param("category", "DEFAULT")
			).andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.number").value(0))
			.andExpect(jsonPath("$.size").value(10))
			.andExpect(jsonPath("$.content.[*].boardId", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.content.[*].title", containsInAnyOrder("title1", "title2", "title3")))
			.andExpect(jsonPath("$.content.[*].content", containsInAnyOrder("content1", "content2", "content3")))
			.andExpect(jsonPath("$.content.[*].image", containsInAnyOrder("image1", "image2", "image3")))
			.andExpect(jsonPath("$.content.[*].viewCount", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.content.[*].boardCategory", containsInAnyOrder("DEFAULT", "DEFAULT", "DEFAULT")))
			.andExpect(jsonPath("$.content.[*].nickname", containsInAnyOrder("nickname1", "nickname2", "nickname3")))
			.andExpect(jsonPath("$.content.[*].commentCount", containsInAnyOrder(1, 2, 3)));

		verify(boardService, times(1)).getBoardList(anyLong(), eq(pageable), any());
	}

	@Test
	@DisplayName("게시판 개별 조회 - 성공")
	void getBoard() throws Exception {
		// Given
		BoardDto result = BoardDto.builder()
			.boardId(1L)
			.title("title")
			.content("content")
			.imageUrl("image")
			.viewCount(1000)
			.boardCategory(BoardCategory.DEFAULT)
			.commentCount(3)
			.comments(getCommentsDto())
			.build();
		when(boardService.getBoard(gameId, boardId)).thenReturn(result);

		// When & Then
		mockMvc.perform(get("/games/{gameId}/boards/{boardId}", gameId, boardId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.boardId").value(1))
			.andExpect(jsonPath("$.title").value("title"))
			.andExpect(jsonPath("$.content").value("content"))
			.andExpect(jsonPath("$.imageUrl").value("image"))
			.andExpect(jsonPath("$.viewCount").value(1000))
			.andExpect(jsonPath("$.boardCategory").value("DEFAULT"))
			.andExpect(jsonPath("$.commentCount").value(3))
			.andExpect(jsonPath("$.comments.[*].commentId", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.comments.[*].comment", containsInAnyOrder("comment1", "comment2", "comment3")))
			.andExpect(jsonPath("$.comments.[*].userDto.nickname",
				containsInAnyOrder("nickname1", "nickname2", "nickname3")));

		verify(boardService, times(1)).getBoard(gameId, boardId);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 작성 - 성공")
	void createBoard() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("title", "content", BoardCategory.DEFAULT);
		doNothing().when(boardService).createBoard(gameId, request, userId);

		// When & Then
		mockMvc.perform(post("/games/{gameId}/boards", gameId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		verify(boardService, times(1)).createBoard(eq(gameId), any(BoardSaveRequest.class), eq(userId));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 작성 실패 - no title")
	void createBoardNoTitle() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("", "content", BoardCategory.DEFAULT);

		// When & Then
		mockMvc.perform(post("/games/{gameId}/boards", gameId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(boardService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 작성 실패 - no content")
	void createBoardNoContent() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("title", "", BoardCategory.DEFAULT);

		// When & Then
		mockMvc.perform(post("/games/{gameId}/boards", gameId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(boardService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 작성 실패 - no category")
	void createBoardNoCategory() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("title", "content", null);

		// When & Then
		mockMvc.perform(post("/games/{gameId}/boards", gameId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(boardService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 수정 - 성공")
	void updateBoard() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("title", "content", BoardCategory.DEFAULT);
		doNothing().when(boardService).updateBoard(gameId, boardId, request, userId);

		// When & Then
		mockMvc.perform(put("/games/{gameId}/boards/{boardId}", gameId, boardId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		verify(boardService, times(1)).updateBoard(eq(gameId), eq(boardId), any(BoardSaveRequest.class), eq(userId));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 수정 실패 - no title")
	void updateBoardNoTitle() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("", "content", BoardCategory.DEFAULT);

		// When & Then
		mockMvc.perform(put("/games/{gameId}/boards/{boardId}", gameId, boardId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(boardService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 수정 실패 - no content")
	void updateBoardNoContent() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("title", "", BoardCategory.DEFAULT);

		// When & Then
		mockMvc.perform(put("/games/{gameId}/boards/{boardId}", gameId, boardId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(boardService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 수정 실패 - no Category")
	void updateBoardNoCategory() throws Exception {
		// Given
		BoardSaveRequest request = getRequest("title", "content", null);

		// When & Then
		mockMvc.perform(put("/games/{gameId}/boards/{boardId}", gameId, boardId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(boardService);
	}

	@Test
	@WithCustomMockUser
	@DisplayName("게시판 삭제 - 성공")
	void deleteBoard() throws Exception {
		// Given
		doNothing().when(boardService).deleteBoard(gameId, boardId, userId);

		// When & Then
		mockMvc.perform(delete("/games/{gameId}/boards/{boardId}", gameId, boardId))
			.andExpect(status().isOk());

		verify(boardService, times(1)).deleteBoard(eq(gameId), eq(boardId), eq(userId));
	}

}