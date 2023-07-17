package bg.sofia.uni.fmi.mjt.news.articles;

import bg.sofia.uni.fmi.mjt.news.utils.Validations;

public record Source(String id, String name) {
    @Override
    public boolean equals(Object obj) {
        Source other = (Source) obj;
        return (Validations.areTwoObjNull(other.id, this.id) || other.id.equals(this.id) ) &&
                (Validations.areTwoObjNull(other.name, this.name) || other.name.equals(this.name) );
    }
}
