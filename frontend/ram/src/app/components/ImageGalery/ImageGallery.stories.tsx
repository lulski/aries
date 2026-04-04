import type { Meta, StoryObj } from "@storybook/nextjs-vite";

import ImageGallery from "./ImageGallery";

const meta = {
  title: "Components/ImageGallery",
  component: ImageGallery,
  tags: ["autodocs"],
} satisfies Meta<typeof ImageGallery>;

export default meta;

type Story = StoryObj<typeof meta>;

export const withoutImages: Story = {
  args: {
    // numImages: ,
  },
  render: (args) => <ImageGallery {...args} />,
};

export const withImages: Story = {
  args: {
    numImages: 10,
  },

  render: (args) => <ImageGallery {...args} />,
};
