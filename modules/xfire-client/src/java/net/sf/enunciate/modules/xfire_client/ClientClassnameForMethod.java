package net.sf.enunciate.modules.xfire_client;

import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.TypeMirror;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Converts a fully-qualified class name to its alternate client fully-qualified class name.
 *
 * @author Ryan Heaton
 */
public class ClientClassnameForMethod extends ClientPackageForMethod {

  private final boolean jdk15;

  public ClientClassnameForMethod(LinkedHashMap<String, String> conversions) {
    this(conversions, true);
  }

  public ClientClassnameForMethod(LinkedHashMap<String, String> conversions, boolean jdk15) {
    super(conversions);

    this.jdk15 = jdk15;
  }

  @Override
  protected String convert(TypeMirror typeMirror) {
    boolean isArray = typeMirror instanceof ArrayType;
    String conversion = super.convert(typeMirror);

    //if we're using converting to a java 5+ client code, take into account the type arguments.
    if ((this.jdk15) && (typeMirror instanceof DeclaredType)) {
      DeclaredType declaredType = (DeclaredType) typeMirror;
      Collection<TypeMirror> actualTypeArguments = declaredType.getActualTypeArguments();
      if (actualTypeArguments.size() > 0) {
        StringBuilder typeArgs = new StringBuilder("<");
        Iterator<TypeMirror> it = actualTypeArguments.iterator();
        while (it.hasNext()) {
          TypeMirror mirror = it.next();
          typeArgs.append(convert(mirror));
          if (it.hasNext()) {
            typeArgs.append(", ");
          }
        }
        typeArgs.append(">");
      }
    }

    if (isArray) {
      conversion += "[]";
    }
    return conversion;

  }

  @Override
  protected String convert(TypeDeclaration declaration) {
    String convertedPackage;
    PackageDeclaration pckg = declaration.getPackage();
    if (pckg == null) {
      convertedPackage = "";
    }
    else {
      convertedPackage = super.convert(pckg.getQualifiedName());
    }

    return convertedPackage + declaration.getSimpleName();
  }

  @Override
  protected String convert(PackageDeclaration packageDeclaration) {
    throw new UnsupportedOperationException("packages don't have a client classname.");
  }

}