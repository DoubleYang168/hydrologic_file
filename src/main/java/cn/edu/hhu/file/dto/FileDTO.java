package cn.edu.hhu.file.dto;

public class FileDTO
{
    private String name;
    private Long lastModified;
    private Long size;

    public String getName() {
        return this.name;
    }

    public Long getLastModified() {
        return this.lastModified;
    }

    public Long getSize() {
        return this.size;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setLastModified(final Long lastModified) {
        this.lastModified = lastModified;
    }

    public void setSize(final Long size) {
        this.size = size;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FileDTO)) {
            return false;
        }
        final FileDTO other = (FileDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$lastModified = this.getLastModified();
        final Object other$lastModified = other.getLastModified();
        Label_0102: {
            if (this$lastModified == null) {
                if (other$lastModified == null) {
                    break Label_0102;
                }
            }
            else if (this$lastModified.equals(other$lastModified)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$size = this.getSize();
        final Object other$size = other.getSize();
        if (this$size == null) {
            if (other$size == null) {
                return true;
            }
        }
        else if (this$size.equals(other$size)) {
            return true;
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FileDTO;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $lastModified = this.getLastModified();
        result = result * 59 + (($lastModified == null) ? 43 : $lastModified.hashCode());
        final Object $size = this.getSize();
        result = result * 59 + (($size == null) ? 43 : $size.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "FileDTO(name=" + this.getName() + ", lastModified=" + this.getLastModified() + ", size=" + this.getSize() + ")";
    }
}
