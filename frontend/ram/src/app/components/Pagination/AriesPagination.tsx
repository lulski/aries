"use client";
import { Pagination } from "@mantine/core";
import { usePathname, useRouter, useSearchParams } from "next/navigation";

export interface AriesPaginationProps {
  total: number;
  page: number;
  size: number;
}

export default function AriesPagination({
  total,
  page,
  size,
}: AriesPaginationProps) {
  const searchParams = useSearchParams();
  const router = useRouter();
  const pathname = usePathname();

  const handlePageChange = (page: number) => {
    const params = new URLSearchParams(searchParams);
    params.set("page", page.toString());

    router.push(`${pathname}?${params.toString()}`);
  };

  return (
    <Pagination
      total={Math.ceil(total / size)}
      value={page}
      onChange={handlePageChange}
    ></Pagination>
  );
}
