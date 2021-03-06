package com.hackathon.MYD.model.review.service;

import com.hackathon.MYD.model.review.ReviewEntity;
import com.hackathon.MYD.model.review.dto.ReviewDto;
import com.hackathon.MYD.model.review.repository.ReviewRepository;
import com.hackathon.MYD.model.store.StoreEntity;
import com.hackathon.MYD.model.store.repository.StoreRepository;
import com.hackathon.MYD.model.user.UserEntity;
import com.hackathon.MYD.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * ServiceImpl
 * 결합도를 느슨하게 함.
 *
 * @author 전지환
 */
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * 매장별 리뷰를 저장한다.
     *
     * @param storeIdx
     * @param nickname
     * @param reviewRequestDto
     * @throws Exception
     */
    @Transactional
    public void saveReview(Long storeIdx, String nickname, ReviewDto.ReviewRequestDto reviewRequestDto) throws Exception {
        // 유저가 존재하는지 확인한다.
        UserEntity userEntity = userRepository.findByNickname(nickname);

        // 매장이 존재하는지 확인한다.
        StoreEntity storeEntity = storeRepository.findByStoreIdx(storeIdx);
        if (storeEntity==null) throw new Exception("해당 매장을 찾을 수 없음");

        // Review 생성하기
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .reviewContent(reviewRequestDto.getReviewContent())
                .perfect(0)
                .notMuch(0)
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .build();

        reviewRepository.save(reviewEntity);
    }

    /**
     * 매장별 리뷰를 읽어온다.
     *
     * @param storeIdx
     * @return List<ReviewEntity>
     */
    public List<ReviewDto.ReviewResponseDto> getStoreReview(Long storeIdx){
        return reviewRepository.findAllByStoreEntity_StoreIdx(storeIdx)
                .stream().map(ReviewDto.ReviewResponseDto::of).collect(Collectors.toList());
    }

    /**
     * 해당 리뷰에 공감을 추가한다.
     *
     * @param reviewIdx
     */
    @Transactional
    public void addLike(Long reviewIdx){
        ReviewEntity targetReview = reviewRepository.findByReviewIdx(reviewIdx);
        targetReview.setPerfect(targetReview.getPerfect()+1);
    }

    /**
     * 해당 리뷰에 공감을 취소한다.
     *
     * @param reviewIdx
     */
    @Transactional
    public void cancelLike(Long reviewIdx) throws Exception {
        ReviewEntity targetReview = reviewRepository.findByReviewIdx(reviewIdx);

        Integer existPerfect = targetReview.getPerfect();
        if (existPerfect <= 0) throw new Exception("공감을 더이상 취소 할 수 없습니다.");

        targetReview.setPerfect(existPerfect-1);
    }

    /**
     * 해당 리뷰에 비공감을 추가한다.
     *
     * @param reviewIdx
     */
    @Transactional
    public void addDislike(Long reviewIdx){
        ReviewEntity targetReview = reviewRepository.findByReviewIdx(reviewIdx);
        targetReview.setNotMuch(targetReview.getNotMuch()+1);
    }

    /**
     * 해당 리뷰에 비공감을 취소한다.
     *
     * @param reviewIdx
     */
    @Transactional
    public void cancelDislike(Long reviewIdx) throws Exception {
        ReviewEntity targetReview = reviewRepository.findByReviewIdx(reviewIdx);

        Integer existNotMuch = targetReview.getNotMuch();
        if (existNotMuch <= 0) throw new Exception("비공감을 더이상 취소 할 수 없습니다.");

        targetReview.setNotMuch(existNotMuch-1);
    }
}
