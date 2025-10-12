"use client";

import { Button, Group, SimpleGrid } from "@mantine/core";
import { useSearchParams } from "next/navigation";
import { useEffect, useMemo, useRef, useState } from "react";
import { ErrorBanner } from "../components/ErrorBanner/Error";
import AriesPagination, {
  AriesPaginationProps,
} from "../components/Pagination/AriesPagination";
import PostInline from "../components/Post/PostInline";
import { PostApiResponse } from "../lib/postsApiCall";

type PageProps = {
  searchParams?: Promise<{
    page?: string;
    size?: string;
  }>;
};

async function getPosts(
  currentPage: number,
  size: number
): Promise<PostApiResponse> {
  const API_BFF_POST_URL = "/api/posts";
  const fetchUrl = `${API_BFF_POST_URL}?page=${currentPage}&size=${size}`;
  const response = await fetch(fetchUrl);

  if (!response.ok) {
    throw new Error("Failed to fetch posts");
  }
  const responseJson: PostApiResponse = await response.json();

  return responseJson;
}

export default function Posts({ searchParams }: PageProps) {
  const searchParam = useSearchParams();
  const page = searchParam.get("page") || "1";
  const size = searchParam.get("size") || "10";
  const [error, setError] = useState<string | null>(null);

  // memoize parsed numeric values
  const pageNum = useMemo(() => parseInt(page, 10) || 1, [page]);
  const sizeNum = useMemo(() => parseInt(size, 10) || 10, [size]);

  // simple in-component cache to memoize API responses per page/size
  const cacheRef = useRef<Record<string, PostApiResponse>>({});

  const cacheKey = useMemo(() => `${pageNum}-${sizeNum}`, [pageNum, sizeNum]);

  const [posts, setPosts] = useState<PostApiResponse | null>(null);
  const [pagination, setPagination] = useState<AriesPaginationProps | null>(
    null
  );

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setError(null);

      // return cached response if present
      if (cacheRef.current[cacheKey]) {
        const cached = cacheRef.current[cacheKey];
        if (!cancelled) {
          setPosts(cached);
          setPagination({
            total: cached.total ?? 0,
            page: pageNum,
            size: sizeNum,
          });
        }
        return;
      }

      try {
        const response = await getPosts(pageNum, sizeNum);
        cacheRef.current[cacheKey] = response;
        if (!cancelled) {
          // console.log(
          //   Date.now().toString() + " >>> useEffect getting posts from cache: ",
          //   cacheKey,
          //   pageNum,
          //   sizeNum
          // );
          setPosts(response);
          setPagination({
            total: response.total ?? 0,
            page: pageNum,
            size: sizeNum,
          });
        }
      } catch (err: any) {
        if (!cancelled) {
          setError(err?.message ?? "Failed to fetch posts");
          setPosts(null);
          setPagination(null);
        }
      }
    }

    load();

    return () => {
      cancelled = true;
    };
  }, [cacheKey, pageNum, sizeNum]);

  const [showNewButton, setShowNewButton] = useState<boolean | null>(null);
  useEffect(() => {
    const getSession = async () => {
      const res = await fetch("/api/me");
      if (!res.ok) {
        console.info("user is not logged in");
        setShowNewButton(false);
      } else {
        const { session } = await res.json();
        if (session?.isAuthenticated && session.authorities.includes("ADMIN")) {
          setShowNewButton(true);
        } else {
          setShowNewButton(false);
        }
      }
    };

    getSession();
  }, []);

  if (error) return <ErrorBanner message={error} onRetry={null} />;

  if (!posts) return <div>Loading...</div>;

  return (
    <>
      {showNewButton && (
        <SimpleGrid>
          <Group justify="flex-end">
            <Button
              component="a"
              href="/posts/new"
              bottom={"10px"}
              pos={"relative"}
            >
              New Post
            </Button>
          </Group>
        </SimpleGrid>
      )}
      <SimpleGrid>
        {posts.postDto.map((post, index) => (
          <PostInline {...post} key={index}></PostInline>
        ))}
      </SimpleGrid>

      <Group justify="center">
        {pagination && posts.total > 0 && <AriesPagination {...pagination} />}
      </Group>
    </>
  );
}
